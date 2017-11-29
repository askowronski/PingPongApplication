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

//    @Test
//    public void testWritePlayerArrayToJson() {
//
//        String username = "jaboi";
//        String filePath = "pingPongPlayerTest.txt";
//
//        PlayerPersistenceManager pPM = new PlayerPersistenceManager(filePath);
//        Player player = new Player(new EloRating(),1,username,"jah","boi");
//        List<Player> players = new ArrayList<>();
//        players.add(player);
//
//        String json = pPM.writePlayerArrayToJson(players);
//
//        String expectedContents = "[{\"id\":1,\"newUsername\":\"jaboi\",\"eloRating\":{\"rating\":1500.0}}]";
//
//        assertTrue(json.equals(expectedContents));
//
//    }

//    @Test
//    public void testFindPlayersAsOfAGame() {
//        GamePersistenceManager gPM = new GamePersistenceManager();
//        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
//        List<PingPongGame> games = gPM.getGamesView();
//        PingPongGame game = new PingPongGame(games.get(0).getiD(),games.get(0).getPlayer1(),
//                games.get(0).getPlayer2(),games.get(0).getPlayer1Score(),games.get(0).getPlayer2Score());
//
//        List<Player> players = pPM.getPlayersPriorToAGame(game);
//
//        assertTrue(players.size() == pPM.getViewPlayers().size());
//    }

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
