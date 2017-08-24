package PersistenceManagerTest;


import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.PersistenceModel.PersistencePlayer;
import app.ViewModel.EloRating;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
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

        String expectedContents = "[{\"id\":1,\"newUsername\":\"jaboi\",\"eloRating\":{\"rating\":1500.0}}]";

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
        List<Player> playersRead = pPM.getViewPlayers();

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

        Player playerRead = pPM.getPlayerByIDOld(3);

        assertTrue(player3.equals(playerRead));

    }

    @Test
    public void testFindPlayers() {

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

        List<Player> playersRead = pPM.getViewPlayers();

        assertEquals(players.get(0),playersRead.get(0));
        assertEquals(players.get(1),playersRead.get(1));
        assertEquals(players.get(2),playersRead.get(2));


    }

    @Test
    public void testFindPlayersAsOfAGame() {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        List<PingPongGame> games = gPM.getGamesView();
        PingPongGame game = new PingPongGame(games.get(0).getiD(),games.get(0).getPlayer1(),
                games.get(0).getPlayer2(),games.get(0).getPlayer1Score(),games.get(0).getPlayer2Score());

        List<Player> players = pPM.getPlayersPriorToAGame(game);

        assertTrue(players.size() == pPM.getViewPlayers().size());
    }

    @Test
    public void testReadViewPlayers() {

        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        List<Player> viewPlayers = pPM.getViewPlayers();
        List<PersistencePlayer> players = pPM.getPlayersNew();

        for(int i = 0; i < viewPlayers.size(); i++) {
            assertTrue(viewPlayers.get(i).getiD() == players.get(i).getId());
            assertTrue(viewPlayers.get(i).getUsername().equals(players.get(i).getUsername()));
        }
    }




}
