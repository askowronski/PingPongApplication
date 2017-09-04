package app.PersistenceModel;


import app.ViewModel.Player;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PersistenceGame {

    @JsonProperty("iD") private final int iD;
    @JsonProperty("player1ID") private final int player1ID;
    @JsonProperty("player2ID") private final int player2ID;
    @JsonProperty("score1") private final int player1Score;
    @JsonProperty("score2") private final int player2Score;
    @JsonProperty("time") private final Date time;

    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown=true)
    public PersistenceGame(@JsonProperty("iD") int iD,@JsonProperty("player1ID")  int player1ID,
            @JsonProperty("player2ID") int player2ID,
            @JsonProperty("score1") int score1,
            @JsonProperty("score2") int score2,
            @JsonProperty("time") Date time){
        this.iD=iD;
        this.player1ID=player1ID;
        this.player2ID=player2ID;
        this.player1Score=score1;
        this.player2Score=score2;
        this.time = time;
    }

    public PersistenceGame(int iD,int player1ID, int player2ID, int score1, int score2) {
        this.iD = iD;
        this.player1ID = player1ID;
        this.player2ID = player2ID;
        this.player1Score = score1;
        this.player2Score = score2;
        this.time = new Date();
    }

    public PersistenceGame() {
        this.iD = 0;
        this.player1ID = 0;
        this.player2ID = 0;
        this.player1Score = 0;
        this.player2Score = 0;
        this.time = new Date();
    }

    public int getiD() {
        return iD;
    }

    public int getPlayer1ID() {
        return player1ID;
    }

    public int getPlayer2ID() {
        return player2ID;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public Date getTime() {
        return time;
    }

    public boolean didWin(Player player){
        if(this.getPlayer1ID() == player.getiD()){
            return this.getPlayer1Score() > this.getPlayer2Score();
        }
        if(this.getPlayer2ID() == player.getiD()){
            return this.getPlayer2Score() > this.getPlayer1Score();
        }
        return false;
    }

    public boolean didLose(Player player){
        if(this.getPlayer1ID() == player.getiD()){
            return this.getPlayer1Score() < this.getPlayer2Score();
        }
        if(this.getPlayer2ID() == player.getiD()){
            return this.getPlayer2Score() < this.getPlayer1Score();
        }
        return false;
    }



    @Override
    public boolean equals(Object o){
        if(!(o instanceof PersistenceGame)) {
            return false;
        }
        PersistenceGame game2 = (PersistenceGame) o;
        return this.getiD() == game2.getiD()
                && this.getPlayer1ID() == game2.getPlayer1ID()
                && this.getPlayer2ID() == game2.getPlayer2ID()
                && this.getPlayer1Score() == game2.getPlayer1Score()
                && this.getPlayer2Score() == game2.getPlayer2Score()
                && this.getTime().equals(game2.getTime());
    }
}
