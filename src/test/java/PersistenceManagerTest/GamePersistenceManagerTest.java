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


    //i need to mock out eloRatingPM as currently its getting ratings based off of active players...


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
