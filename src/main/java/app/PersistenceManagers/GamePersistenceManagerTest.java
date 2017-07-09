package app.PersistenceManagers;


import app.EloRating;
import app.PingPongGame;
import app.Player;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;


public class GamePersistenceManagerTest {

    @Test
    public void testWriteGameToJson(){
        Player player1 = new Player(new EloRating(), 1,"ka");
        Player player2 = new Player(new EloRating(), 2,"sweet");

        PingPongGame game = new PingPongGame(1,player1,player2,15,13);
        GamePersistenceManager gPM = new GamePersistenceManager();
        String output = gPM.writeGameToJson(game);

        assertTrue(output.equals("{id:1,player1:1,player2:2,score1:15,score2:13}"));
    }

//    @Test
//    public void testWriteGameToFile() {
//        Player player1 = new Player(new EloRating(), 1,"ka");
//        Player player2 = new Player(new EloRating(), 2,"sweet");
//
//        PingPongGame game = new PingPongGame(1,player1,player2,15,13);
//        GamePersistenceManager gPM = new GamePersistenceManager("C:\\Users\\askowronski\\documents\\pingpong2.txt");
//        String output = gPM.writeGameToJson(game);
//        gPM.writeGameToFile(game);
//        String output2 = gPM.getFile().readFile();
//
//        assertTrue(output2.equals(output));
//    }
}
