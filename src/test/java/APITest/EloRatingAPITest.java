package APITest;


import app.PersistenceModel.PersistenceEloRating;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EloRatingAPITest {

    public static String USERNAME1 = "gameTestUser1";
    public static String USERNAME2 = "gameTestUser2";

    public static Player player1;
    public static Player player2;

    public static List<Integer> playersToDelete = new ArrayList<>();

    public static PlayerApiRequester playerApi = new PlayerApiRequester();
    public static GameApiRequester gameApi = new GameApiRequester();
    public static EloRatingApiRequester eloApi = new EloRatingApiRequester();

    @BeforeAll
    public static void beforeAll() throws IOException {

        playerApi.createPlayer(USERNAME1);
        playerApi.createPlayer(USERNAME2);

        player1 = playerApi.getPlayer(USERNAME1);
        player2 = playerApi.getPlayer(USERNAME2);

        playersToDelete.add(player1.getiD());
        playersToDelete.add(player2.getiD());

        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14);
    }

    @AfterAll
    public static void afterAll() throws IOException {
        List<PingPongGame> games = gameApi.getGamesForPlayer(player1.getiD());

        for (PingPongGame game : games) {
            gameApi.deleteGame(game.getiD());
        }

        for (Integer id : playersToDelete) {
            playerApi.hardDeletePlayer(id);
        }
    }

    @Test
    public void testEloRatingCreationUponPlayerCreation() throws IOException {
        playerApi.createPlayer("swagTesterEloRatingMan");
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

        gameApi.editGameScore1(games.get(games.size() - 1).getiD(),15);

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
    public void testEloRatingReOrderUponGameReorder() throws  IOException {
        List<PingPongGame> gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());


    }

}
