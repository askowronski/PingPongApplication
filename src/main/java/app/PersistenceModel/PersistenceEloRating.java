package app.PersistenceModel;


import app.ViewModel.EloRating;
import app.ViewModel.GameOutcomeEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PersistenceEloRating {

    @JsonProperty("rating") private final double eloRating;
    @JsonProperty("playerID") private final int playerID;
    @JsonProperty("gameID") private final int gameID;

    @JsonCreator
    public PersistenceEloRating(@JsonProperty("rating") double eloRating, @JsonProperty("playerID") int playerID,
            @JsonProperty("gameID") int gameID) {
        this.eloRating = eloRating;
        this.playerID = playerID;
        this.gameID = gameID;
    }

    public double getEloRating() {
        return eloRating;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getGameID() {
        return gameID;
    }

    public double expectedScore(PersistenceEloRating rating2){
        return this.getEloRating() / (this.getEloRating()+rating2.getEloRating());
    }

    public PersistenceEloRating newRating(GameOutcomeEnum outcome, PersistenceEloRating rating2,int gameID) {
        double expectedScore = this.expectedScore(rating2);
        return new PersistenceEloRating(this.getEloRating() + EloRating.K_FACTOR * (outcome.getFactor() - expectedScore),
                this.getPlayerID(),gameID);
    }

    public PersistenceEloRating newRating(GameOutcomeEnum outcome, PersistenceEloRating rating2) {
        double expectedScore = this.expectedScore(rating2);
        return new PersistenceEloRating(this.getEloRating() + EloRating.K_FACTOR * (outcome.getFactor() - expectedScore),
                this.getPlayerID(),gameID+1);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof PersistenceEloRating)){
            return false;
        }
        PersistenceEloRating rating = (PersistenceEloRating) o;
        return this.getEloRating() == rating.getEloRating()
                && this.getPlayerID() == rating.getPlayerID()
                && this.getGameID() == rating.getGameID();
    }
}
