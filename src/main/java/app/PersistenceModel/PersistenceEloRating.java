package app.PersistenceModel;


import app.ViewModel.EloRating;
import app.ViewModel.GameOutcomeEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
@Table(name = "Elo_Rating")
public class PersistenceEloRating {


    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @JsonProperty("rating")
    @Column(name = "eloRating")
    private  double eloRating;

    @JsonProperty("playerID")
    @Column(name = "playerId")
    private  int playerID;

    @JsonProperty("gameID")
    @Column(name = "gameId")
    private  int gameID;

    @JsonCreator
    public PersistenceEloRating(@JsonProperty("rating") double eloRating, @JsonProperty("playerID") int playerID,
            @JsonProperty("gameID") int gameID) {
        this.eloRating = eloRating;
        this.playerID = playerID;
        this.gameID = gameID;
    }

    public PersistenceEloRating(){

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEloRating(double eloRating) {
        this.eloRating = eloRating;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
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
