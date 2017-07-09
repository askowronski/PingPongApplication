package app.PersistenceManagers;

import app.StatsEngine.EloRating;
import app.StatsEngine.Player;
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




    public void writePlayerToFile(String name) {

    }

    public Player getPlayer(int id){
        //call to DB to return a player
        return new Player(new EloRating(),id,"ja");
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




}
