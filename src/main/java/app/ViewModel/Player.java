package app.ViewModel;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {

    @JsonProperty("eloRating") EloRating rating;
    @JsonProperty("id") int iD;
    @JsonProperty("username") String username;
    @JsonProperty("firstName") String firstName;
    @JsonProperty("lastName") String lastName;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonCreator
    public Player(@JsonProperty("EloRating") EloRating rating,
            @JsonProperty("id") int iD,
            @JsonProperty("username") String username,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName) {
        this.rating = rating;
        this.iD = iD;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public EloRating getRating() {
        return rating;
    }

    public void setRating(EloRating rating) {
        this.rating = rating;
    }

    public int getiD() {
        return iD;
    }

    public void setiD(int iD) {
        this.iD = iD;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        if (!(o instanceof Player)) {
            return false;
        }
        Player player2 = (Player) o;
        return this.getiD() == player2.getiD() &&
                this.getFirstName().equals(player2.getFirstName()) &&
                this.getLastName().equals(player2.getLastName()) &&
                this.getUsername().equals(player2.getUsername()) &&
                this.getRating().getRating() == player2.getRating().getRating();
    }
}
