package app.API.GraphData;

import app.PingPongModel.PingPongGame;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.DecimalFormat;

/**
 * Created by askowronski on 8/8/17.
 */
public class EloRatingGraphData {

    @JsonProperty(value="game") public final PingPongGame game;
    @JsonProperty(value="eloRating") public final double eloRating;
    @JsonProperty(value="opponentEloRating") public final double opponentEloRating;
    @JsonProperty(value="label") public final int label;

    @JsonCreator
    public EloRatingGraphData(@JsonProperty(value="game") PingPongGame game,
                              @JsonProperty(value="label") int label,
                                   @JsonProperty(value="eloRating") double eloRating,
                                   @JsonProperty(value="opponentEloRating") double opponentEloRating) {

        DecimalFormat df = new DecimalFormat("#.##");
        this.game=game;
        this.eloRating=Double.valueOf(df.format(eloRating));
        this.opponentEloRating=Double.valueOf(df.format(opponentEloRating));
        this.label=label;

    }

    public PingPongGame getGame() {
        return game;
    }

    public double getEloRating() {
        return this.eloRating;
    }

    public double getOpponentEloRating() {
        return this.opponentEloRating;
    }
}
