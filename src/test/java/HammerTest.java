import APITest.GameApiRequester;

import java.io.IOException;
import java.util.Random;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HammerTest {

    GameApiRequester gameApi = new GameApiRequester();

    @Test
    public void create1000Games() throws IOException{
        Random random = new Random();
        for (int i = 0; i < 40 ; i++){
            gameApi.createGame(1,2,random.nextInt(15 - 10 + 1) + 10
                    ,random.nextInt(15 - 10 + 1) + 10,"2017OCT09");
        }
    }

}
