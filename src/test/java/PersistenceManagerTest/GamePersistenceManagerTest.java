package PersistenceManagerTest;


import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceModel.PersistenceGame;
import app.ViewModel.EloRating;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


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

//    @Test
//    public void testReadGameFromJson(){
//        Player player1 = new Player(new EloRating(), 1,"ka");
//        Player player2 = new Player(new EloRating(), 2,"sweet");
//
//        String filePath = "pingPongTest.txt";
//        PingPongGame game = new PingPongGame(1,player1,player2,15,13);
//        GamePersistenceManager gPM = new GamePersistenceManager(filePath);
//        gPM.writeGameToFileOld(game);
//
//        List<PingPongGame> games = gPM.getGamesView();
//
//
//
//        assertTrue(game.equals(games.get(0)));
//    }

    @Test
    public void testEditGameScore2(){

    }


    //i need to mock out eloRatingPM as currently its getting ratings based off of active players...
    @Test
    public void testGetViewGames(){
        Player player1 = new Player(new EloRating(), 1,"ka");
        Player player2 = new Player(new EloRating(), 3,"sweet");

        String filePath = "pingPongTest.txt";
        PersistenceGame game = new PersistenceGame(1,1,3,15,13);
        List<PersistenceGame> games = new ArrayList<>();
        games.add(game);
        GamePersistenceManager gPM = new GamePersistenceManager(filePath);
        gPM.getFile().writeFile("",false);
        gPM.getFile().writeFile(gPM.writeGamesToJsonNew(games),false);

        List<PersistenceGame> persistenceGames = gPM.getGamesNew();
        List<PingPongGame> viewGames = gPM.getGamesView();

        for(int i = 0; i < persistenceGames.size(); i++){
            assertTrue(persistenceGames.get(i).getiD() == viewGames.get(i).getiD());
            assertTrue(persistenceGames.get(i).getPlayer1ID() == viewGames.get(i).getPlayer1().getiD());
            assertTrue(persistenceGames.get(i).getPlayer2ID() == viewGames.get(i).getPlayer2().getiD());
            assertTrue(persistenceGames.get(i).getPlayer1Score() == viewGames.get(i).getPlayer1Score());
            assertTrue(persistenceGames.get(i).getPlayer2Score() == viewGames.get(i).getPlayer2Score());
        }
    }

    @Test
    public void testReorderGames() {
        List<PersistenceGame> games = new ArrayList<>();

        PersistenceGame game1 = new PersistenceGame(1,2,
                2,15,14,new Date(2017,9,13));
        PersistenceGame game2 = new PersistenceGame(2,2,2,
                15,14,new Date(2017,10,13));
        PersistenceGame game3 = new PersistenceGame(3,2,2,
                15,14,new Date(2017,11,13));

        games.add(game1);
        games.add(game2);
        games.add(game3);

        PersistenceGame game4 = new PersistenceGame(4,2,2,
                15,14,new Date(2017,10,15));

        GamePersistenceManager gPM = new GamePersistenceManager();
        games = gPM.reorderGames(games,game4);

        assertEquals(games.get(2),game4);


    }



}
