package APITest;

import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameAPITest {

    public static String USERNAME1 = "gameTestUser1";
    public static String USERNAME2 = "gameTestUser2";

    public static Player player1;
    public static Player player2;

    public static PlayerApiRequester playerApi = new PlayerApiRequester();
    public static GameApiRequester gameApi = new GameApiRequester();

    @BeforeAll
    public static void beforeAll() throws IOException {

        playerApi.createPlayer(USERNAME1);
        playerApi.createPlayer(USERNAME2);

        player1 = playerApi.getPlayer(USERNAME1);
        player2 = playerApi.getPlayer(USERNAME2);

        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14);
    }

    @AfterAll
    public static void afterAll() throws  IOException {
        List<PingPongGame> games = gameApi.getGamesForPlayer(player1.getiD());

        for(PingPongGame game:games) {
            gameApi.deleteGame(game.getiD());
        }

        playerApi.hardDeletePlayer(player1.getiD());
        playerApi.hardDeletePlayer(player2.getiD());
    }


    // Create
    @Test
    public void createGameTest() throws IOException {

        HttpResponse response = gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14);

        assertTrue(ApiUtil.retrieveMessage(response).equals("Game Created"));
    }

    // Exceptions

    @Test
    public void createGameWithSamePlayerTest() throws  IOException {
        HttpResponse response = gameApi.createGame(player1.getiD(),player1.getiD(), 15, 14);

        assertTrue(ApiUtil.retrieveMessage(response).equals("Players must be different."));
    }

    @Test
    public void createGameWithInvalidScoreTest() throws  IOException {
        HttpResponse response = gameApi.createGame(player1.getiD(),player2.getiD(), -1, 14);

        assertTrue(ApiUtil.retrieveMessage(response).equals("Scores must be greater than or equal to 0."));
    }

    @Test
    public void createGameWithInvalidScorePlayer2() throws IOException {
        HttpResponse response = gameApi.createGame(player1.getiD(),player2.getiD(), 5, -1);

        assertTrue(ApiUtil.retrieveMessage(response).equals("Scores must be greater than or equal to 0."));
    }

    @Test
    public void createGameWithDateInFuture() throws IOException {
        HttpResponse response = gameApi.createGame(player1.getiD(),player2.getiD(), 5, 15, "2018MAR1");

        assertTrue(ApiUtil.retrieveMessage(response).equals("Date cannot be greater than now."));
    }

    @Test
    public void createGameWithPlayerIdThatDoesNotExist() throws IOException {
        HttpResponse response = gameApi.createGame(-1,player2.getiD(), 5, 15);

        assertTrue(ApiUtil.retrieveMessage(response).equals("No Player found with ID -1."));
    }

    // Read

    @Test
    public void getGamesTest() throws  IOException {
        List<PingPongGame> games = gameApi.getGames();

        assertTrue(games.size() > 0);
    }


    // Update

    @Test
    public void editGameToPlayersWithSameIdTest() throws IOException {
        List<PingPongGame> games = gameApi.getGames();
        HttpResponse response = null;
        for(PingPongGame game: games) {
            if(game.getPlayer2().getiD() == player2.getiD()) {
                response = gameApi.editGame(game.getiD(),player2.getiD());
            }
        }
        String message = ApiUtil.retrieveMessage(response);

        assertTrue(message.equals("Players must be different."));
    }

    @Test
    public void editGameToInvalidScore() throws IOException {
        List<PingPongGame> games = gameApi.getGames();
        HttpResponse response = null;
        for(PingPongGame game: games) {
            if(game.getPlayer2().getiD() == player2.getiD()) {
                response = gameApi.editGameScore1(game.getiD(),-1);
            }
        }
        String message = ApiUtil.retrieveMessage(response);

        assertTrue(message.equals("Scores must be greater than or equal to 0."));
    }

    @Test
    public void editGameSwitchPlayers() throws  IOException {
        gameApi.createGame(player1.getiD(), player2.getiD(), 15, 14);
        List<PingPongGame> gamesPrior = gameApi.getGames();

        gameApi.editGame(gamesPrior.get(gamesPrior.size()-1).getiD(),player2.getiD(), player1.getiD());

        List<PingPongGame> gamesAfter = gameApi.getGames();

        PingPongGame game = gamesAfter.get(gamesAfter.size()-1);

        assertTrue(game.getPlayer1().getiD() == player2.getiD());
        assertTrue(game.getPlayer2().getiD() == player1.getiD());
    }

    @Test
    public void editGameNewDate() throws  IOException {
        List<PingPongGame> games = gameApi.getGamesForPlayer(player1.getiD());

        int gameId = games.get(games.size()-1).getiD();

        gameApi.editGame(gameId, "2011MAR1");

        PingPongGame game = gameApi.getGame(gameId);

        assertEquals(game.getTime().toString(), "Tue Mar 01 00:00:00 CST 2011");
    }

    // Delete

    @Test
    public void deleteGame() throws  IOException {
        List<PingPongGame> gamesForPlayer = gameApi.getGamesForPlayer(player1.getiD());

        gameApi.deleteGame(gamesForPlayer.get(0).getiD());


    }


}
