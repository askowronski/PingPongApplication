package app.ViewModel;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class EloRating {

    public static double DEFAULT_RATING = 1500.00;
    public static double K_FACTOR = 16;

    @JsonProperty("rating") private final double rating;

    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonCreator
    public EloRating (@JsonProperty("rating") double rating) {
        this.rating=rating;
    }

    public EloRating() {
        this.rating=this.DEFAULT_RATING;
    }

    public double expectedScore(EloRating rating2){
        return this.getRating()/(this.getRating()+rating2.getRating());
    }

    public double getRating(){
        return this.rating;
    }

    public EloRating newRating(GameOutcomeEnum outcome, EloRating rating2) {
        double expectedScore = this.expectedScore(rating2);
           return new EloRating(this.getRating() + this.K_FACTOR * (outcome.getFactor() - expectedScore));

    }
}
