package app.StatsEngine;

import app.ViewModel.GameOutcomeEnum;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;

import java.util.ArrayList;
import java.util.List;


public class SinglePlayerStatisticsCalculator {

    private final List<PingPongGame> games;
    private final Player player;

    public SinglePlayerStatisticsCalculator(List<PingPongGame> games , Player player) {
        this.games=games;
        this.player=player;
    }

    public List<PingPongGame> getGames() {
        return this.games;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getHighestScore() {
        List<Integer> scores = new ArrayList<>();
        List<PingPongGame> games = this.getGames();
        int maxValue = games.get(0).getPlayerScore(this.getPlayer());

        for(int i =0; i< games.size(); i++){
            int score = games.get(i).getPlayerScore(this.getPlayer());
            scores.add(score);
            if(score>maxValue){
                maxValue=score;
            }
        }
        return maxValue;
    }

    public double calculateStdDev() {
        List<Integer> scores = this.getScores();
        int averageNumerator = 0;
        double stdDevSum = 0;

        for(int i = 0; i < scores.size(); i++){
            averageNumerator += scores.get(i);
        }

        double average = (double) averageNumerator/scores.size();

        for(int i = 0; i < scores.size(); i++){
            stdDevSum += Math.pow((scores.get(i)-average),2);
        }

        return Math.sqrt(stdDevSum/scores.size());
    }

    public List<Integer> getScores() {
        List<Integer> scores = new ArrayList<>();
        List<PingPongGame> games = this.getGames();

        for(int i = 0; i < games.size(); i++){
            scores.add(games.get(i).getPlayerScore(this.getPlayer()));
        }
        return scores;
    }

    public int getWins() {
        List<Integer> scores = new ArrayList<>();
        List<PingPongGame> games = this.getGames();
        int wins = 0;

        for(int i = 0; i < games.size(); i++){
           if(games.get(i).didWin(this.getPlayer())){
               wins++;
           }
        }
        return wins;
    }

    public int gamesPlayed() {
        return this.getGames().size();
    }

    public int getLosses(){
        List<Integer> scores = new ArrayList<>();
        List<PingPongGame> games = this.getGames();
        int losses = 0;

        for(int i = 0; i < games.size(); i++){
            if(games.get(i).didLose(this.getPlayer())){
                losses++;
            }
        }
        return losses;
    }

    public double getAverageScore() {
        double numerator=0.0;
        List<PingPongGame> games = this.getGames();
        for(PingPongGame game:games) {
            if(game.getPlayer1().getiD() == this.getPlayer().getiD()){
                numerator+=game.getPlayer1Score();
            }
            if(game.getPlayer2().getiD() == this.getPlayer().getiD()){
                numerator+=game.getPlayer2Score();
            }
        }
        return numerator/(double)games.size();
    }

    public List<GameOutcomeEnum> getOutcomes() {
        List<GameOutcomeEnum> outcomes = new ArrayList<>();
        List<PingPongGame> games = this.getGames();

        for(PingPongGame game:games){
            if(game.didWin(this.getPlayer())){
                outcomes.add(GameOutcomeEnum.WIN);
            } else if(game.didLose(this.getPlayer())){
                outcomes.add(GameOutcomeEnum.LOSS);
            } else {
                outcomes.add(GameOutcomeEnum.DRAW);
            }
        }
        return outcomes;
    }

    public double getOpponentAverageScore(){
        double numerator=0.0;
        List<PingPongGame> games = this.getGames();
        for(PingPongGame game:games) {
            if(game.getPlayer1().getiD() == this.getPlayer().getiD()){
                numerator+=game.getPlayer2Score();
            }
            if(game.getPlayer2().getiD() == this.getPlayer().getiD()){
                numerator+=game.getPlayer1Score();
            }
        }
        return numerator/(double)games.size();
    }
}
