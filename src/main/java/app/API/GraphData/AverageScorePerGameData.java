package app.API.GraphData;


import app.ViewModel.PingPongGame;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.DecimalFormat;
import java.util.Date;

public class AverageScorePerGameData {

    @JsonProperty(value = "averageScore") public final double averageScore;
    @JsonProperty(value = "score") public final double score;
    @JsonProperty(value = "opponentAverageScore") public final double opponentAverageScore;
    @JsonProperty(value = "opponentScore") public final double opponentScore;
    @JsonProperty(value = "game") public final PingPongGame game;
    @JsonProperty(value = "label") public final int label;
    @JsonProperty(value = "beginningTime") public final Date beginningTime;
    @JsonProperty(value = "endTime") public final Date endTime;

    @JsonCreator
    public AverageScorePerGameData(@JsonProperty(value = "averageScore") double averageScore,
            @JsonProperty(value = "score") double score,
            @JsonProperty(value = "opponentAverageScore") double opponentAverageScore,
            @JsonProperty(value = "opponentScore") double opponentScore,
            @JsonProperty(value = "game") PingPongGame game,
            @JsonProperty(value = "label") int label,
            @JsonProperty(value = "beginningTime") Date beginningTime,
            @JsonProperty(value = "endTime") Date endTime) {

        DecimalFormat df = new DecimalFormat("#.##");
        this.averageScore = Double.valueOf(df.format(averageScore));
        this.score = score;
        this.opponentAverageScore = Double.valueOf(df.format(opponentAverageScore));
        this.opponentScore = opponentScore;
        this.game = game;
        this.label = label;
        this.beginningTime = beginningTime;
        this.endTime = endTime;

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

    public int getLabel() {
        return label;
    }


    public Date getBeginningTime() {
        return beginningTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}
