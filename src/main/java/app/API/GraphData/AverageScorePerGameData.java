package app.API.GraphData;


import app.PingPongModel.PingPongGame;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AverageScorePerGameData {

    @JsonProperty(value="averageScore") public final double averageScore;
    @JsonProperty(value="score") public final double score;
    @JsonProperty(value="opponentAverageScore") public final double opponentAverageScore;
    @JsonProperty(value="opponentScore") public final double opponentScore;
    @JsonProperty(value="game") public final PingPongGame game;
    @JsonProperty(value="label") public final int label;

    @JsonCreator
    public AverageScorePerGameData(@JsonProperty(value="averageScore") double averageScore,
                                   @JsonProperty(value="score") double score,
                                   @JsonProperty(value="opponentAverageScore") double opponentAverageScore,
                                   @JsonProperty(value="opponentScore") double opponentScore,
                                    @JsonProperty(value="game") PingPongGame game,
                                   @JsonProperty(value="label") int label) {
        this.averageScore=averageScore;
        this.score=score;
        this.opponentAverageScore=opponentAverageScore;
        this.opponentScore =opponentScore;
        this.game=game;
        this.label=label;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public double getScore() {
        return score;
    }

    public double getOpponentAverageScore() {
        return opponentAverageScore;
    }

    public double getOpponentScore() {
        return opponentScore;
    }

    public PingPongGame getGame() {
        return game;
    }
}
