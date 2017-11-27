package APITest;


import app.PersistenceModel.PersistenceEloRating;
import app.PersistenceModel.PersistenceGame;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EloRatingAPITest {

    public static String USERNAME1 = "gameTestUser1";
    public static String USERNAME2 = "gameTestUser2";
    public static String FIRST_NAME = "first";
    public static String LAST_NAME = "last";

    public static Player player1;
    public static Player player2;

    public static List<Integer> playersToDelete = new ArrayList<>();

    public static PlayerApiRequester playerApi = new PlayerApiRequester();
    public static GameApiRequester gameApi = new GameApiRequester();
    public static EloRatingApiRequester eloApi = new EloRatingApiRequester();

    @BeforeEach
    public void beforeEach() throws IOException {

        playerApi.createPlayer(USERNAME1, FIRST_NAME, LAST_NAME);
        playerApi.createPlayer(USERNAME2, FIRST_NAME, LAST_NAME);

        player1 = playerApi.getPlayer(USERNAME1);
        player2 = playerApi.getPlayer(USERNAME2);

        playersToDelete.add(player1.getiD());
        playersToDelete.add(player2.getiD());

        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14);
    }

    @AfterEach
    public void afterEach() throws IOException {
        for (Integer i : playersToDelete) {

            List<PingPongGame> games = gameApi.getGamesForPlayer(i);

            if (games.size() > 0) {
                for (PingPongGame game : games) {
                    HttpResponse response = gameApi.deleteGame(game.getiD());
                }
            }
        }

        for (Integer id : playersToDelete) {
            HttpResponse response = playerApi.hardDeletePlayer(id);
        }

        playersToDelete = new ArrayList<>();
    }

    @Test
    public void testEloRatingCreationUponPlayerCreation() throws IOException {
        playerApi.createPlayer("swagTesterEloRatingMan", FIRST_NAME, LAST_NAME);
        Player player = playerApi.getPlayer("swagTesterEloRatingMan");

        List<PersistenceEloRating> ratings = eloApi.getRatings(player.getiD());

        playersToDelete.add(player.getiD());

        assertTrue(ratings.size() == 1);
        assertTrue(ratings.get(0).getEloRating() == 1500.00);
    }

    @Test
    public void testEloRatingCreationUponGameCreation() throws IOException {
        // game created in before all

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());

        assertTrue(ratings.size() > 1);
    }

    @Test
    public void testEloRatingAccuracyAfterOneWin() throws IOException {
        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());
        List<PersistenceEloRating> ratings2 = eloApi.getRatings(player2.getiD());

        assertTrue(ratings.get(1).getEloRating() == 1508.00);
        assertTrue(ratings2.get(1).getEloRating() == 1492.00);
    }

    @Test
    public void testEloRatingAccuracyAfterGameOneEditToOppositeOutcome() throws IOException {
        List<PingPongGame> games = gameApi.getGamesForPlayer(player1.getiD());

        gameApi.editGameScore1(games.get(0).getiD(), 13);

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());
        List<PersistenceEloRating> ratings2 = eloApi.getRatings(player2.getiD());

        assertTrue(ratings.get(1).getEloRating() == 1492.00);
        assertTrue(ratings2.get(1).getEloRating() == 1508.00);

    }

    @Test
    public void testEloRatingAccuracyAfterGameOneEditToDrawOutcome() throws IOException {
        List<PingPongGame> games = gameApi.getGamesForPlayer(player1.getiD());

        gameApi.editGameScore1(games.get(0).getiD(), 14);

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());
        List<PersistenceEloRating> ratings2 = eloApi.getRatings(player2.getiD());

        assertTrue(ratings.get(1).getEloRating() == 1500.00);
        assertTrue(ratings2.get(1).getEloRating() == 1500.00);

    }

    @Test
    public void testEloRatingAccuracyAfterGameOneEditAfterSwitchingPlayers() throws IOException {
        List<PingPongGame> games = gameApi.getGamesForPlayer(player1.getiD());

        gameApi.editGameScore1(games.get(games.size() - 1).getiD(), 15);

        gameApi.editGame(games.get(games.size() - 1).getiD(), player2.getiD(), player1.getiD());

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());
        List<PersistenceEloRating> ratings2 = eloApi.getRatings(player2.getiD());

        assertTrue(ratings.get(1).getEloRating() == 1492.0);
        assertTrue(ratings2.get(1).getEloRating() == 1508.0);
    }

    @Test
    public void testEloRatingCascadeDeletion() throws IOException {
        List<PingPongGame> games = gameApi.getGamesForPlayer(player1.getiD());

        for (PingPongGame game : games) {
            gameApi.deleteGame(game.getiD());
        }

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());

        assertTrue(ratings.size() == 1);
        assertTrue(ratings.get(0).getGameID() == 0);
        assertTrue(ratings.get(0).getEloRating() == 1500.00);
    }

    // game date edit

    @Test
    public void testEloRatingGameIdReOrderUponGameReorderToBeginning() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14);

        List<PingPongGame> gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());
        if (gamesForPlayer.size() == 1) {
            gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14);
            gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());
        }

        gameApi.editGame(gamesForPlayer.get(1).getiD(), "2016MAR05+00:00");

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());

        assertTrue(ratings.get(1).getGameID() == gamesForPlayer.get(1).getiD());
    }

    @Test
    public void testEloRatingGameIdReOrderUponGameReorderToMiddle() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14);

        List<PingPongGame> gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());
        if (gamesForPlayer.size() == 1) {
            gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");
            gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV03");

            gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());
        }

        gameApi.editGame(gamesForPlayer.get(0).getiD(), "2017NOV02");

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());

        gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());

        for (int i = 0; i < gamesForPlayer.size(); i++) {
            assertTrue(gamesForPlayer.get(i).getiD() == ratings.get(i + 1).getGameID());
        }
    }

    @Test
    public void testEloRatingGameIdReOrderUponGameReorderToEnd() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14);

        List<PingPongGame> gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());
        if (gamesForPlayer.size() == 1) {
            gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");
            gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV03");

            gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());
        }

        gameApi.editGame(gamesForPlayer.get(0).getiD(), "2017NOV04");

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());

        gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());

        for (int i = 0; i < gamesForPlayer.size(); i++) {
            assertTrue(gamesForPlayer.get(i).getiD() == ratings.get(i + 1).getGameID());
        }
    }

    @Test
    public void testEloRatingReOrderUponGameReorderToBeginning() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14);

        List<PingPongGame> gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());

        gameApi.editGame(gamesForPlayer.get(1).getiD(), "2016MAR05");

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());

        Double start = 1500.00;
        for (PersistenceEloRating rating : ratings) {
            assertTrue(rating.getEloRating() > (start - .5) &&
                    rating.getEloRating() < (start + .5));
            start += 8.0;
        }
    }

    @Test
    public void testEloRatingReOrderUponGameReorderToMiddle() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV03");

        List<PingPongGame> gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());

        gameApi.editGame(gamesForPlayer.get(0).getiD(), "2017NOV02+00:00");

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());

        Double start = 1500.00;
        for (PersistenceEloRating rating : ratings) {
            assertTrue(rating.getEloRating() > (start - .5) &&
                    rating.getEloRating() < (start + .5));
            start += 8.0;
        }
    }

    @Test
    public void testEloRatingReOrderUponGameReorderToEnd() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV03");

        List<PingPongGame> gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());

        gameApi.editGame(gamesForPlayer.get(0).getiD(), "2017NOV04");

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());

        Double start = 1500.00;
        for (PersistenceEloRating rating : ratings) {
            assertTrue(rating.getEloRating() > (start - .5) &&
                    rating.getEloRating() < (start + .5));
            start += 8.0;
        }
    }


    @Test
    public void testEloRatingsPlayer1OnCreationOfGamePriorToExistingOnes() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017SEP01");

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());

        assertTrue(ratings.get(0).getEloRating() == 1500);
        assertTrue(ratings.get(1).getEloRating() == 1508.0);
        assertTrue(ratings.get(2).getEloRating() > 1515);
    }

    @Test
    public void testEloRatingsPlayer2OnCreationOfGamePriorToExistingOnes() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017SEP01");

        List<PersistenceEloRating> ratings = eloApi.getRatings(player2.getiD());

        assertTrue(ratings.get(0).getEloRating() == 1500);
        assertTrue(ratings.get(1).getEloRating() == 1492);
        assertTrue(ratings.get(2).getEloRating() < 1485);
    }

    @Test
    public void testEloRatingsPlayer1OnEditOfGamePriorToExistingOnes() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV02");

        List<PingPongGame> games = gameApi.getGamesForPlayer(player1.getiD());
        gameApi.editGame(games.get(games.size() - 1).getiD(), "2017SEP01+00:00");

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());

        assertTrue(ratings.get(0).getEloRating() == 1500);
        assertTrue(ratings.get(1).getEloRating() == 1508.0);
        assertTrue(ratings.get(2).getEloRating() > 1515);
        assertTrue(ratings.get(0).getGameID() == 0);
        assertTrue(ratings.get(1).getGameID() == games.get(games.size() - 1).getiD());
        assertTrue(ratings.get(2).getGameID() == games.get(0).getiD());
    }

    @Test
    public void testEloRatingsPlayer2OnEditOfGamePriorToExistingOnes() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017SEP01");

        List<PingPongGame> games = gameApi.getGamesForPlayer(player1.getiD());
        gameApi.editGame(games.get(games.size() - 1).getiD(), "2017SEP01+00:00");

        List<PersistenceEloRating> ratings = eloApi.getRatings(player2.getiD());

        assertTrue(ratings.get(0).getEloRating() == 1500);
        assertTrue(ratings.get(1).getEloRating() == 1492);
        assertTrue(ratings.get(2).getEloRating() < 1485);

        assertTrue(ratings.get(1).getGameID() == games.get(games.size() - 1).getiD());
        assertTrue(ratings.get(2).getGameID() == games.get(0).getiD());
    }

    @Test
    public void testEloRatingDeletionAfterEditGameRemovePlayer1() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");

        List<PingPongGame> games = gameApi.getGamesForPlayer(player1.getiD());

        String USERNAME3 = "gameTestUser3";

        playerApi.createPlayer(USERNAME3, FIRST_NAME, LAST_NAME);

        Player player3 = playerApi.getPlayer(USERNAME3);
        playersToDelete.add(player3.getiD());

        gameApi.editGame(games.get(games.size() - 1).getiD(), player3.getiD());

        List<PersistenceEloRating> ratings1 = eloApi.getRatings(player1.getiD());

        assertTrue(ratings1.size() == games.size());
        assertTrue(ratings1.get(ratings1.size() - 1).getGameID() != games.get(games.size() - 1)
                .getiD());
    }

    @Test
    public void testEloRatingDeletionAfterEditGameRemovePlayer2() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");

        List<PingPongGame> games = gameApi.getGamesForPlayer(player2.getiD());

        String USERNAME3 = "gameTestUser3";

        playerApi.createPlayer(USERNAME3, FIRST_NAME, LAST_NAME);

        Player player3 = playerApi.getPlayer(USERNAME3);
        playersToDelete.add(player3.getiD());

        gameApi.editGamePlayer2(games.get(games.size() - 1).getiD(), player3.getiD());

        List<PersistenceEloRating> ratings = eloApi.getRatings(player2.getiD());

        assertTrue(ratings.size() == games.size());
        assertTrue(
                ratings.get(ratings.size() - 1).getGameID() != games.get(games.size() - 1).getiD());
    }

    @Test
    public void testEloRatingRecalculateAfterDeleteGameInMiddleOfListPlayer1() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");

        List<PingPongGame> games = gameApi.getGamesForPlayer(player2.getiD());

        gameApi.deleteGame(games.get(games.size() - 2).getiD());

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());

        assertTrue(ratings.size() == games.size());
        assertTrue(ratings.get(2).getEloRating() > 1515 &&
                ratings.get(2).getEloRating() < 1516);
        assertTrue(ratings.get(1).getEloRating() > 1507 &&
                ratings.get(1).getEloRating() < 1509);
        assertTrue(
                ratings.get(ratings.size() - 1).getGameID() == games.get(games.size() - 1).getiD());
    }

    @Test
    public void testEloRatingRecalculateAfterDeleteGameInMiddleOfListPlayer2() throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");

        List<PingPongGame> games = gameApi.getGamesForPlayer(player2.getiD());

        gameApi.deleteGame(games.get(games.size() - 2).getiD());

        List<PersistenceEloRating> ratings = eloApi.getRatings(player2.getiD());

        assertTrue(ratings.size() == games.size());
        assertTrue(ratings.get(2).getEloRating() > 1484 &&
                ratings.get(2).getEloRating() < 1486);
        assertTrue(ratings.get(1).getEloRating() > 1491 &&
                ratings.get(1).getEloRating() < 1493);
        assertTrue(
                ratings.get(ratings.size() - 1).getGameID() == games.get(games.size() - 1).getiD());
    }

    @Test
    public void testEloRatingRecalculateAfterDeleteGameAtBeginningOfListOfListPlayer1()
            throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");

        List<PingPongGame> games = gameApi.getGamesForPlayer(player2.getiD());

        gameApi.deleteGame(games.get(0).getiD());

        List<PersistenceEloRating> ratings = eloApi.getRatings(player1.getiD());

        assertTrue(ratings.size() == games.size());
        assertTrue(ratings.get(2).getEloRating() > 1515 &&
                ratings.get(2).getEloRating() < 1516);
        assertTrue(ratings.get(1).getEloRating() > 1507 &&
                ratings.get(1).getEloRating() < 1509);
        assertTrue(
                ratings.get(ratings.size() - 1).getGameID() == games.get(games.size() - 1).getiD());
    }

    @Test
    public void testEloRatingRecalculateAfterDeleteGameAtBeginningOfListPlayer2()
            throws IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14, "2017NOV01");

        List<PingPongGame> games = gameApi.getGamesForPlayer(player2.getiD());

        gameApi.deleteGame(games.get(0).getiD());

        List<PersistenceEloRating> ratings = eloApi.getRatings(player2.getiD());

        assertTrue(ratings.size() == games.size());
        assertTrue(ratings.get(2).getEloRating() > 1484 &&
                ratings.get(2).getEloRating() < 1486);
        assertTrue(ratings.get(1).getEloRating() > 1491 &&
                ratings.get(1).getEloRating() < 1493);
        assertTrue(
                ratings.get(ratings.size() - 1).getGameID() == games.get(games.size() - 1).getiD());
    }
}
