package app.PersistenceModel;

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
@Table(name = "Player")
public class PersistencePlayer {

    @JsonProperty("id")
    @Id
    @Column(name = "id", updatable = false)
    private int id;
    @JsonProperty("username")
    @Column(name = "username")
    private String username;
    @JsonProperty("deleted")
    @Column(name = "deleted")
    private int deleted;

    @JsonCreator
    public PersistencePlayer(@JsonProperty("id") int id,@JsonProperty("username") String username) {
        this.id = id;
        this.username = username;
        this.deleted = 0;
    }

    public PersistencePlayer() {

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

    public void setId(int id) {
        this.id = id;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
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
