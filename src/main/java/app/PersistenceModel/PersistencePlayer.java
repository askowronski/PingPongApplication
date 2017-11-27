package app.PersistenceModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Player")
public class PersistencePlayer {

    @JsonProperty("id")
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private int id;
    @JsonProperty("username")
    @Column(name = "username")
    private String username;
    @JsonProperty("deleted")
    @Column(name = "deleted")
    private int deleted;
    @JsonProperty("firstName")
    @Column(name = "first_name")
    private String firstName;
    @JsonProperty("lastName")
    @Column(name = "last_name")
    private String lastName;

    @JsonCreator
    public PersistencePlayer(@JsonProperty("id") int id, @JsonProperty("username") String username,
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName) {
        this.id = id;
        this.username = username;
        this.deleted = 0;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PersistencePlayer)) {
            return false;
        }
        PersistencePlayer player2 = (PersistencePlayer) o;
        return this.getId() == player2.getId() &&
                this.deleted == player2.deleted &&
                this.getFirstName().equals(player2.getFirstName()) &&
                this.getLastName().equals(player2.getLastName()) &&
                this.getUsername().equals(player2.getUsername());
    }
}
