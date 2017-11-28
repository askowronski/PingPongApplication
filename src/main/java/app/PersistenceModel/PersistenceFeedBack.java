package app.PersistenceModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
@Table(name = "Feedback")
public class PersistenceFeedBack {


    @Id
    @GeneratedValue
    @Column(name = "id")
    @JsonProperty("id")
    private int id;

    @JsonProperty("player_id")
    @Column(name = "player_Id")
    private  int playerId;

    @Lob

    @JsonProperty("feedback_text")
    @Column(name = "feedback_text", nullable = false)
    private String feedBack;


    public PersistenceFeedBack(){

    }

    public PersistenceFeedBack(int playerId, String feedBack) {
        this.playerId = playerId;
        this.feedBack = feedBack;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
