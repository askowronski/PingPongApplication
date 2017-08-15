package app.ViewModel;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Player {

    @JsonProperty("eloRating") final EloRating rating;
    @JsonProperty("id") final int iD;
    @JsonProperty("username") final String username;

    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonCreator
    public Player(@JsonProperty("EloRating")EloRating rating,
                  @JsonProperty("id") int iD,
                  @JsonProperty("username") String username){
        this.rating=rating;
        this.iD=iD;
        this.username=username;
    }

    public EloRating getEloRating() {
        return rating;
    }

    public int getiD() {
        return iD;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Player)) {
            return false;
        }
        Player player2 = (Player)o;
        return this.getiD() == player2.getiD();
    }
}
