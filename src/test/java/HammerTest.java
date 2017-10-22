import APITest.GameApiRequester;

import java.io.IOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HammerTest {

    GameApiRequester gameApi = new GameApiRequester();

    @Test
    public void create1000Games() throws IOException{
        for (int i = 0; i < 1001 ; i++){
            gameApi.createGame(1,2,14,15,"2017OCT10");
        }
    }

}
