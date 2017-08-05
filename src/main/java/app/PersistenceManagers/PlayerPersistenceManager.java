package app.PersistenceManagers;

import app.PingPongModel.EloRating;
import app.PingPongModel.GameOutcomeEnum;
import app.PingPongModel.PingPongGame;
import app.PingPongModel.Player;
import app.ReadWriteFile.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PlayerPersistenceManager {

    public static String FILE_PATH = "players.txt";

    private final File file;

    public PlayerPersistenceManager() {
        this.file = new File(FILE_PATH);
    }

    public PlayerPersistenceManager(String filePath) {
        this.file = new File(filePath);
    }

    public void writePlayerToFile(Player player) {
        try{
            List<Player> currentPlayers = this.readPlayerArrayFromJson(this.getFile().readFile());
            currentPlayers.add(player);
            this.getFile().writeFile(this.writePlayerArrayToJson(currentPlayers),false);
        } catch(NullPointerException e){
            List<Player> players = new ArrayList<>();
            players.add(player);
            this.getFile().writeFile(this.writePlayerArrayToJson(players),false);
        }
    }

    public void writePlayersToFile(List<Player> players) {
        this.getFile().writeFile(this.writePlayerArrayToJson(players),false);
    }




    public void writePlayerToFile(String name) {

    }

    public String writePlayerToJson(Player player) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.writeValueAsString(player);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public File getFile() {
        return this.file;
    }

    public int getNextID() {
        String json = this.readFile();
        if(json.equals("No Players Found")){
            return 1;
        }
        List<Player> players = this.readPlayerArrayFromJson(json);

        List<Integer> ids = new ArrayList<>();
        int highestID = 0;
        for(Player player:players){
            if(player.getiD()>highestID){
                highestID=player.getiD();
            }
        }
        return highestID+1;
    }

    public boolean checkUsernameExists(String username) {
        List<Player> players = this.getPlayers();
        List<String> usernames = new ArrayList<>();

        for(Player player:players) {
            usernames.add(player.getUsername());
        }
        return usernames.contains(username);
    }


    public String writePlayerArrayToJson(List<Player> players) {
        ObjectMapper mapper = new ObjectMapper();

        try{
            return mapper.writeValueAsString(players);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public List<Player> readPlayerArrayFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Player>> mapType;
        mapType = new TypeReference<List<Player>>(){};
        try {
            List<Player> players = mapper.readValue(json, mapType);
            return players;
        } catch(IOException e){
            List<Player> players = new ArrayList<Player>();
            e.printStackTrace();
            return players;
        }
    }

    public String readFile() {
        try{
            return this.getFile().readFile();
        } catch(NullPointerException e){
            return "No Players Found";
        }
    }

    public Player getPlayerByID(int id){
        List<Player> players = this.readPlayerArrayFromJson(this.getFile().readFile());
        for(Player player:players){
            if(player.getiD()==id){
                return player;
            }
        }
        return new Player(new EloRating(0),0,"Player Does Not Exist");
    }

    public Player getPlayerByUsername(String username){
        List<Player> players = this.readPlayerArrayFromJson(this.getFile().readFile());
        for(Player player:players){
            if(player.getUsername().equals(username)){
                return player;
            }
        }
        return new Player(new EloRating(0),0,"Player Does Not Exist");
    }

    public List<Player> getPlayers() {
        String json = this.readFile();
        List<Player> players = new ArrayList<>();
        if(json.equals("No Players Found")){
            return players;
        }
        return this.readPlayerArrayFromJson(json);
    }

    public boolean editPlayer(int id,String newUsername) {
        Player player = this.getPlayerByID(id);

        if(player.getiD()==0) {
            return false;
        }

        Player newPlayer = new Player(player.getEloRating(),id,newUsername);

        List<Player> players = this.getPlayers();

        players.set(players.indexOf(player),newPlayer);

        GamePersistenceManager gPM = new GamePersistenceManager();
        gPM.editPlayerUsername(newPlayer);

        this.getFile().writeFile(this.writePlayerArrayToJson(players),false);
        return true;

    }

    public boolean deletePlayer(int id) {
        Player player = this.getPlayerByID(id);

        if(player.getiD()==0) {
            return false;
        }

        List<Player> players = this.getPlayers();

        players.remove(player);

        this.getFile().writeFile(this.writePlayerArrayToJson(players),false);
        return true;

    }

    public boolean deletePlayer(String username) {
        Player player = this.getPlayerByUsername(username);

        if(player.getiD()==0) {
            return false;
        }

        List<Player> players = this.getPlayers();

        players.remove(player);

        this.getFile().writeFile(this.writePlayerArrayToJson(players),false);
        return true;

    }

    public Player getPlayerWithHighestRating() {
        List<Player> players = this.getPlayers();
        Player highestPlayer = players.get(0);
        for(int i = 0; i< players.size(); i++) {
            if(players.get(i).getEloRating().getRating()>highestPlayer.getEloRating().getRating()){
                highestPlayer= players.get(i);
            }
        }
        return highestPlayer;
    }

    public void updatePlayersEloRating(Player player1PriorToGame, Player player2PriorToGame,PingPongGame game) {
        List<Player> allPlayers = this.getPlayersPriorToAGame(game);

        Player player1 = player1PriorToGame;
        Player player2 = player2PriorToGame;

        int indexPlayer1 = allPlayers.indexOf(player1);
        int indexPlayer2 = allPlayers.indexOf(player2);
        Player newPlayer1;
        Player newPlayer2;

        if(game.getPlayer1Score()>game.getPlayer2Score()) {
            EloRating newRating1 = player1.getEloRating().newRating(GameOutcomeEnum.WIN,player2.getEloRating());
            EloRating newRating2 = player2.getEloRating().newRating(GameOutcomeEnum.LOSS,player1.getEloRating());
            newPlayer1 = new Player(newRating1,player1.getiD(),player1.getUsername());
            newPlayer2 = new Player(newRating2,player2.getiD(),player2.getUsername());
            allPlayers.set(indexPlayer1,newPlayer1);
            allPlayers.set(indexPlayer2,newPlayer2);
        } else if(game.getPlayer2Score()>game.getPlayer1Score()) {
            EloRating newRating1 = player1.getEloRating().newRating(GameOutcomeEnum.LOSS,player2.getEloRating());
            EloRating newRating2 = player2.getEloRating().newRating(GameOutcomeEnum.WIN,player1.getEloRating());
            newPlayer1 = new Player(newRating1,player1.getiD(),player1.getUsername());
            newPlayer2 = new Player(newRating2,player2.getiD(),player2.getUsername());
            allPlayers.set(indexPlayer1,newPlayer1);
            allPlayers.set(indexPlayer2,newPlayer2);
        } else {
            EloRating newRating1 = player1.getEloRating().newRating(GameOutcomeEnum.DRAW,player2.getEloRating());
            EloRating newRating2 = player2.getEloRating().newRating(GameOutcomeEnum.DRAW,player1.getEloRating());
            newPlayer1 = new Player(newRating1,player1.getiD(),player1.getUsername());
            newPlayer2 = new Player(newRating2,player2.getiD(),player2.getUsername());
            allPlayers.set(indexPlayer1,newPlayer1);
            allPlayers.set(indexPlayer2,newPlayer2);
        }
        this.writePlayersToFile(allPlayers);
    }

    public List<Player> getPlayersPriorToAGame(PingPongGame game) {
        List<Player> currentPlayers = this.getPlayers();
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> currentGames = gPM.getGames();
        if(!currentGames.contains(game)){
            return currentPlayers;
        }
        List<PingPongGame> splicedGames = currentGames.subList(0,currentGames.indexOf(game)+1);


        for(int i = 0; i < currentPlayers.size(); i++) {
            Player playerCheck = currentPlayers.get(i);
            for(int j = splicedGames.size()-1; j >=0; j-- ){
                if(splicedGames.get(j).getPlayer1().getiD() == playerCheck.getiD()) {
                    currentPlayers.set(i,splicedGames.get(j).getPlayer1());
                    break;
                }
                if(splicedGames.get(j).getPlayer2().getiD() == playerCheck.getiD()) {
                    currentPlayers.set(i,splicedGames.get(j).getPlayer2());
                    break;
                }
                if(j==0){
                    Player newPlayer = new Player(new EloRating(),currentPlayers.get(i).getiD(),currentPlayers.get(i).getUsername());
                    currentPlayers.set(i,newPlayer);
                }
            }
        }
        return currentPlayers;
    }
}
