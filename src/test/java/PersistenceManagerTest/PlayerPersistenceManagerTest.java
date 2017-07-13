package PersistenceManagerTest;


import app.PersistenceManagers.PlayerPersistenceManager;
import app.StatsEngine.EloRating;
import app.StatsEngine.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class PlayerPersistenceManagerTest {

    @Test
    public void testWritePlayerArrayToJson() {

        String username = "jaboi";
        String filePath = "pingPongPlayerTest.txt";

        PlayerPersistenceManager pPM = new PlayerPersistenceManager(filePath);
        Player player = new Player(new EloRating(),1,username);
        List<Player> players = new ArrayList<>();
        players.add(player);

        String json = pPM.writePlayerArrayToJson(players);

        String expectedContents = "[{\"id\":1,\"username\":\"jaboi\",\"eloRating\":{\"rating\":1500.0}}]";

        assertTrue(json.equals(expectedContents));

    }

    @Test
    public void testReadPlayerArrayToJson() {

        String username = "jaboi";
        String filePath = "pingPongPlayerTest.txt";

        PlayerPersistenceManager pPM = new PlayerPersistenceManager(filePath);
        Player player = new Player(new EloRating(),1,username);
        List<Player> players = new ArrayList<>();
        players.add(player);

        pPM.writePlayerToFile(player);
        List<Player> playersRead = pPM.getPlayers();

        assertTrue(player.equals(playersRead.get(0)));

    }

    @Test
    public void testFindPlayerByUsername() {

        String username = "jaboi";
        String username2 = "matt";
        String username3 = "etal";
        String filePath = "pingPongPlayerTest.txt";

        PlayerPersistenceManager pPM = new PlayerPersistenceManager(filePath);
        Player player = new Player(new EloRating(),1,username);
        Player player2 = new Player(new EloRating(),2,username);
        Player player3 = new Player(new EloRating(),3,username);
        List<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player2);
        players.add(player3);


        pPM.writePlayerToFile(player);
        pPM.writePlayerToFile(player2);
        pPM.writePlayerToFile(player3);

        Player playerRead = pPM.getPlayerByID(3);

        assertTrue(player3.equals(playerRead));

    }

    @Test
    public void testFidndPlayers() {

        String username = "jaboi";
        String username2 = "matt";
        String username3 = "etal";
        String filePath = "pingPongPlayerTest.txt";

        PlayerPersistenceManager pPM = new PlayerPersistenceManager(filePath);
        Player player = new Player(new EloRating(),1,username);
        Player player2 = new Player(new EloRating(),2,username);
        Player player3 = new Player(new EloRating(),3,username);
        List<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player2);
        players.add(player3);

        pPM.getFile().writeFile("",false);

        pPM.writePlayerToFile(player);
        pPM.writePlayerToFile(player2);
        pPM.writePlayerToFile(player3);

        List<Player> playersRead = pPM.getPlayers();

        assertEquals(players.get(0),playersRead.get(0));
        assertEquals(players.get(1),playersRead.get(1));
        assertEquals(players.get(2),playersRead.get(2));


    }




}
