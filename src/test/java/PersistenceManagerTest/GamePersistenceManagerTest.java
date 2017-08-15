package PersistenceManagerTest;


import app.PersistenceManagers.GamePersistenceManager;
import app.ViewModel.EloRating;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertTrue;


public class GamePersistenceManagerTest {

    @Test
    public void testWriteGameToJson(){
        Player player1 = new Player(new EloRating(), 1,"ka");
        Player player2 = new Player(new EloRating(), 2,"sweet");

        String filePath = "pingPongTest.txt";
        PingPongGame game = new PingPongGame(1,player1,player2,15,13);
        GamePersistenceManager gPM = new GamePersistenceManager(filePath);
        String output = gPM.writeCurrentGameToGamesJsonOld(game);

        String expectedResult = "[{\"iD\":1,\"player1\":{\"id\":1,\"newUsername\":\"ka\",\"eloRating\":{\"rating\":1500.0}}," +
                "\"player2\":{\"id\":2,\"newUsername\":\"sweet\",\"eloRating\":{\"rating\":1500.0}},\"score1\":15,\"score2\":13";



        assertTrue(output.contains(expectedResult));
    }

    @Test
    public void testReadGameFromJson(){
        Player player1 = new Player(new EloRating(), 1,"ka");
        Player player2 = new Player(new EloRating(), 2,"sweet");

        String filePath = "pingPongTest.txt";
        PingPongGame game = new PingPongGame(1,player1,player2,15,13);
        GamePersistenceManager gPM = new GamePersistenceManager(filePath);
        gPM.writeGameToFileOld(game);

        List<PingPongGame> games = gPM.getGamesOld();



        assertTrue(game.equals(games.get(0)));
    }

    @Test
    public void testEditGameScore2(){
        Player player1 = new Player(new EloRating(), 1,"ka");
        Player player2 = new Player(new EloRating(), 2,"sweet");

        String filePath = "pingPongTest.txt";
        PingPongGame game = new PingPongGame(1,player1,player2,15,13);
        GamePersistenceManager gPM = new GamePersistenceManager(filePath);
        PingPongGame editGame = new PingPongGame(1,player1,player2,15,14);
        gPM.getFile().writeFile("",false);
        gPM.writeGameToFileOld(game);
        gPM.editWriteGameToFile(game,editGame);
        String fileContents = gPM.readFile();
        String expectedResult = "[{\"iD\":1,\"player1\":{\"id\":1,\"newUsername\":\"ka\",\"eloRating\":{\"rating\":1500.0}}," +
                "\"player2\":{\"id\":2,\"newUsername\":\"sweet\",\"eloRating\":{\"rating\":1500.0}},\"score1\":15,\"score2\":14";



        assertTrue(fileContents.contains(expectedResult));
    }



}
