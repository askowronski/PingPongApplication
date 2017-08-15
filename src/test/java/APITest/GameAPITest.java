package APITest;


import app.API.GameAPI;
import app.PersistenceManagers.GamePersistenceManager;
import app.ReadWriteFile.File;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class GameAPITest {
    private static GameAPITest ourInstance = new GameAPITest();

    public static GameAPITest getInstance() {
        return ourInstance;
    }

    @Test
    public void CreateGameAPITest() {

        int player1ID = 1;
        int player2ID = 2;

        int score1 = 15;
        int score2 = 14;

        String filePath = "pingPongTest.txt";
        File file = new File(filePath);

        GamePersistenceManager gPM = new GamePersistenceManager(filePath);
        GameAPI gameAPI = new GameAPI();
        gameAPI.createGameOld(Integer.toString(player1ID),
                Integer.toString(player2ID),
                Integer.toString(score1),
                Integer.toString(score2));
        String expectedResult = "[{\"id\":1,\"player1\":{\"id\": 0,\"newUsername\":\"Player Does Not Exist\",\"eloRating\":{\"rating\":0.0}}," +
                "\"player2\":{\"id\":0,\"newUsername\":\"Player Does Not Exist\",\"eloRating\":{\"rating\":0.0}},\"score1\":15,\"score2\":14";

        assertTrue(file.readFile().contains(expectedResult));
    }


}
