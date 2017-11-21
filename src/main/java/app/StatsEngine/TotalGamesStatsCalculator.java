package app.StatsEngine;


import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.PersistenceModel.PersistenceGame;
import app.PersistenceModel.PersistencePlayer;
import app.ViewModel.EloRating;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;

import java.util.ArrayList;
import java.util.List;

public class TotalGamesStatsCalculator {

    private final GamePersistenceManager gPM;
    private final PlayerPersistenceManager pPM;
    private final List<PingPongGame> games;
    private final List<Player> players;

    public TotalGamesStatsCalculator() {
        this.gPM = new GamePersistenceManager();
        this.pPM = new PlayerPersistenceManager();
        this.games = gPM.getGamesView();
        this.players = pPM.getViewPlayers();
    }

    public int getTotalGames() {
        return games.size();
    }

    public double averageEloRating() {
        double numerator = 0;

        for(Player player:players){
            numerator+=player.getRating().getRating();
        }
        return numerator/players.size();
    }

    public double stdDeviationOfLosses() {
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

    public PlayerPersistenceManager getPPM() {
        return pPM;
    }

    public LongestStreakData getLongestStreak() {
        int currentHighCount = 0;
        Player currentHighPlayer = new Player(new EloRating(), 0, "Empty Player", "Empty", "Player");

        for (Player player:players) {
            int counter = 0;
            List<PingPongGame> gamesForPlayer = gPM.getGamesForPlayer(player, games);
            int currentHighForPlayer = 0;
            for (PingPongGame game: gamesForPlayer) {
                if (game.didWin(player)) {
                    counter++;
                } else {
                    if (counter > currentHighForPlayer) {
                        currentHighForPlayer = counter;
                    }
                    counter=0;
                }
            }
            if (currentHighForPlayer > currentHighCount) {
                currentHighCount = currentHighForPlayer;
                currentHighPlayer = player;
            }
        }

        return new LongestStreakData(currentHighPlayer, currentHighCount);

    }


}
