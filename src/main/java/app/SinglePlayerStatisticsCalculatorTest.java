package app;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;


public class SinglePlayerStatisticsCalculatorTest {

    @Test
    public void testGetHighestScore() {
        Player player1 = new Player(new EloRating(), 1,"ka");
        Player player2 = new Player(new EloRating(), 2,"jah");

        PingPongGame game1 = new PingPongGame(1,player1,player2,15,9);
        PingPongGame game2 = new PingPongGame(1,player1,player2,10,1);
        PingPongGame game3 = new PingPongGame(1,player1,player2,12,10);
        PingPongGame game4 = new PingPongGame(1,player1,player2,19,13);

        List<PingPongGame> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);
        games.add(game3);
        games.add(game4);

        SinglePlayerStatisticsCalculator stats = new SinglePlayerStatisticsCalculator(games,player1);
        assertTrue(stats.getHighestScore() == 19);

        SinglePlayerStatisticsCalculator stats2 = new SinglePlayerStatisticsCalculator(games,player2);
        assertTrue(stats2.getHighestScore() == 13);

    }

    @Test
    public void testStandardDeviation() {

        Player player1 = new Player(new EloRating(), 1,"ka");
        Player player2 = new Player(new EloRating(), 2,"sweet");

        PingPongGame game1 = new PingPongGame(1,player1,player2,15,9);
        PingPongGame game2 = new PingPongGame(1,player1,player2,10,1);
        PingPongGame game3 = new PingPongGame(1,player1,player2,12,10);
        PingPongGame game4 = new PingPongGame(1,player1,player2,19,13);

        List<PingPongGame> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);
        games.add(game3);
        games.add(game4);

        SinglePlayerStatisticsCalculator stats = new SinglePlayerStatisticsCalculator(games,player1);
        double stdDev = stats.calculateStdDev();
        assertTrue(stdDev >3.391 && stdDev < 3.392);

    }

    @Test
    public void testGetWins() {

        Player player1 = new Player(new EloRating(), 1,"ka");
        Player player2 = new Player(new EloRating(), 2,"sweet");

        PingPongGame game1 = new PingPongGame(1,player1,player2,15,9);
        PingPongGame game2 = new PingPongGame(1,player1,player2,10,1);
        PingPongGame game3 = new PingPongGame(1,player1,player2,12,10);
        PingPongGame game4 = new PingPongGame(1,player1,player2,9,13);

        List<PingPongGame> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);
        games.add(game3);
        games.add(game4);

        SinglePlayerStatisticsCalculator stats = new SinglePlayerStatisticsCalculator(games,player1);
        int wins = stats.getWins();
        assertTrue(wins==3);
    }


    @Test
    public void testGetLosses() {

        Player player1 = new Player(new EloRating(), 1,"ka");
        Player player2 = new Player(new EloRating(), 2,"sweet");

        PingPongGame game1 = new PingPongGame(1,player1,player2,15,9);
        PingPongGame game2 = new PingPongGame(1,player1,player2,10,1);
        PingPongGame game3 = new PingPongGame(1,player1,player2,12,10);
        PingPongGame game4 = new PingPongGame(1,player1,player2,9,13);

        List<PingPongGame> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);
        games.add(game3);
        games.add(game4);

        SinglePlayerStatisticsCalculator stats = new SinglePlayerStatisticsCalculator(games,player1);
        int losses = stats.getLosses();
        assertTrue(losses==1);
    }



}
