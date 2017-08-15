package app.PersistenceModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PersistencePlayer {

    @JsonProperty("id") private final int id;
    @JsonProperty("username") private String username;
    @JsonProperty("deleted") private int deleted;

    @JsonCreator
    public PersistencePlayer(@JsonProperty("id") int id,@JsonProperty("username") String username) {
        this.id = id;
        this.username = username;
        this.deleted = 0;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void softDeletePlayer() {
        this.deleted = 1;
    }

    public int getDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof PersistencePlayer)) {
            return false;
        }
        PersistencePlayer player2 = (PersistencePlayer) o;
        return this.getId() == player2.getId();
    }
}
