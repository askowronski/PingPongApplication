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


        for (int i = 0; i < 100 ; i++){
            Random rd = new Random();
            int player2id=0;
            int player1id = rd.nextInt((3 - 1) + 1) + 1;
            if (player1id == 1) {
                player2id = rd.nextInt((3 - 2) + 1) + 2;
            } else if (player1id ==2) {
                player2id = 3;
            } else {
                player2id = 2;
            }

            gameApi.createGame(player1id,player2id,random.nextInt(15 - 10 + 1) + 10
                    ,random.nextInt(15 - 10 + 1) + 10,"2017MAR27");
        }
    }

}
