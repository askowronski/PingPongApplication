package app.ViewModel;


import app.PersistenceModel.PersistencePlayer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PingPongGame {

    @JsonProperty("iD") private final int iD;
    @JsonProperty("player1") private final Player player1;
    @JsonProperty("player2") private final Player player2;
    @JsonProperty("score1") private final int player1Score;
    @JsonProperty("score2") private final int player2Score;
    @JsonProperty("time") private final Date time;
    @JsonProperty("timeString") private final String timeString;

    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonCreator
    public PingPongGame(@JsonProperty("iD") int iD,@JsonProperty("player1")  Player player1,
                        @JsonProperty("player2") Player player2,
                        @JsonProperty("score1") int score1,
                        @JsonProperty("score2") int score2,
            @JsonProperty("time") Date time){
         this.iD=iD;
        this.player1=player1;
        this.player2=player2;
        this.player1Score=score1;
        this.player2Score=score2;
        this.time = time;
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.timeString = sdfDate.format(time);
    }

    public PingPongGame() {
        this.iD=0;
        this.player1= new Player(new EloRating(0),0,"Home Not found","No","Player");
        this.player2= new Player(new EloRating(0),0,"Home Not found", "No", "Player");
        this.player1Score=0;
        this.player2Score=0;
        this.time= new Date();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.timeString = sdfDate.format(time);

    }

    public PingPongGame(int id, Player player1, Player player2, int score1, int score2) {
        this.iD=id;
        this.player1= player1;
        this.player2= player2;
        this.player1Score=score1;
        this.player2Score=score2;
        this.time= new Date();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.timeString = sdfDate.format(time);


    }

    public int getiD() {
        return iD;
    }

    public Date getTime() {
        return this.time;
    }

    public void createAndSave(int iD, Player player1, Player player2, int score1, int score2) {
        PingPongGame game = new PingPongGame(iD,player1,player2,score1,score2);
        String contents = game.writeGameToJson();
    }

    public String writeGameToJson() {
        return "{iD:"+this.getiD()+",player1:"+this.getPlayer1().getiD()
                +",player2:"+this.getPlayer2().getiD()+",score1:"+this.getPlayer1Score()
                +",score2:"+this.getPlayer2Score()+"}";
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public int getPlayerScore(Player player) {
        if(this.getPlayer1().equals(player)){
            return this.getPlayer1Score();
        }
        if(this.getPlayer2().equals(player)){
            return this.getPlayer2Score();
        }
        return 0;
    }

    public boolean didWin(Player player){
        if(this.getPlayer1().getiD() == player.getiD()){
            return this.getPlayer1Score() > this.getPlayer2Score();
        }
        if(this.getPlayer2().getiD() == player.getiD()){
            return this.getPlayer2Score() > this.getPlayer1Score();
        }
        return false;
    }

    public boolean didWin(PersistencePlayer player){
        if(this.getPlayer1().getiD() == player.getId()){
            return this.getPlayer1Score() > this.getPlayer2Score();
        }
        if(this.getPlayer2().getiD() == player.getId()){
            return this.getPlayer2Score() > this.getPlayer1Score();
        }
        return false;
    }

    public boolean didLose(Player player){
        if(this.getPlayer1().getiD() == player.getiD()){
            return this.getPlayer1Score() < this.getPlayer2Score();
        }
        if(this.getPlayer2().getiD() == player.getiD()){
            return this.getPlayer2Score() < this.getPlayer1Score();
        }
        return false;
    }

    public Player getOpponent(Player player) {
        if(this.getPlayer1().getiD() == player.getiD()){
            return this.getPlayer2();
        } else {
            return this.getPlayer1();
        }
    }

    public String getTimeString() {
        return timeString;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof PingPongGame)) {
            return false;
        }
        PingPongGame game2 = (PingPongGame) o;
        return this.getiD() == game2.getiD()
                && this.getPlayer1().equals(game2.getPlayer1())
                && this.getPlayer2().equals(game2.getPlayer2())
                && this.getPlayer1Score() == game2.getPlayer1Score()
                && this.getPlayer2Score() == game2.getPlayer2Score();
    }
}
