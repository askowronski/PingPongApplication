package app.StatsEngine;


import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;

import java.util.ArrayList;
import java.util.List;

public class TotalGamesStatsCalculator {

    private final GamePersistenceManager gPM;

    public TotalGamesStatsCalculator() {
        this.gPM = new GamePersistenceManager();
    }

    public int getTotalGames() {
        List<PingPongGame> games = this.gPM.getGamesView();
        return games.size();
    }

    public double averageEloRating() {
        List<Player> players = new PlayerPersistenceManager().getViewPlayers();

        double numerator = 0;

        for(Player player:players){
            numerator+=player.getRating().getRating();
        }
        return numerator/players.size();
    }

    public double stdDeviationOfLosses() {
        List<PingPongGame> games = this.gPM.getGamesView();

        List<Integer> losingScores = new ArrayList<>();

        for(PingPongGame game:games){
            if(game.getPlayer1Score()>game.getPlayer2Score()){
                losingScores.add(game.getPlayer2Score());
            } else {
                losingScores.add(game.getPlayer1Score());
            }
        }

        double averageNumerator=0;
        for(Integer score:losingScores){
            averageNumerator += score;
        }

        double stdDevSum=0;

        double average = averageNumerator/(double)losingScores.size();

        for(Integer score:losingScores){
            stdDevSum += Math.pow((score-average),2);
        }

        return Math.sqrt(stdDevSum/losingScores.size());

    }

    public double stdDeviationOfGames() {
        List<PingPongGame> games = this.gPM.getGamesView();

        List<Integer> scores = new ArrayList<>();

        for(PingPongGame game:games){
            scores.add(game.getPlayer1Score());
            scores.add(game.getPlayer2Score());
        }

        double averageNumerator=0;
        for(Integer score:scores){
            averageNumerator += score;
        }

        double stdDevSum=0;

        double average = averageNumerator/(double) scores.size();

        for(Integer score:scores){
            stdDevSum += Math.pow((score-average),2);
        }

        return Math.sqrt(stdDevSum/scores.size());

    }

    public GamePersistenceManager getgPM(){
        return this.gPM;
    }


}
