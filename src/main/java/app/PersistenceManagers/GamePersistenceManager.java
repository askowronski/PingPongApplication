package app.PersistenceManagers;


import app.StatsEngine.EloRating;
import app.StatsEngine.PingPongGame;
import app.StatsEngine.Player;
import app.ReadWriteFile.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamePersistenceManager {

    public static String FILE_PATH = "pingpong.txt";

    private final File file;

    public GamePersistenceManager() {
        this.file = new File(FILE_PATH);
    }

    public GamePersistenceManager(String filePath) {
        this.file = new File(filePath);
    }

    public void writeGameToFile(PingPongGame game) {
        this.getFile().writeFile(this.writeGameToJson(game),false);
    }


    public void writeGameToFile(int id, int score1, int score2) {
        Player player1 = new Player(new EloRating(), 1,"ja");
        Player player2 = new Player(new EloRating(), 2,"ja");
        PingPongGame game = new PingPongGame(1, player1, player2, score1, score2);
    }



    public Player getPlayer(int id){
        PlayerPersistenceManager ppm = new PlayerPersistenceManager();
        return ppm.getPlayerByID(id);
    }

    public int getNextID() {
        String json = this.readFile();
        if(json.equals("")){
            return 1;
        }
        List<PingPongGame> games = this.getGames();

        List<Integer> ids = new ArrayList<>();
        int highestID = 0;
        for(PingPongGame game:games){
            if(game.getiD()>highestID){
                highestID=game.getiD();
            }
        }
        return highestID+1;
    }

    public PingPongGame getGameByID(int id) {
        List<PingPongGame> games = this.getGames();
        for(PingPongGame game:games){
            if(game.getiD()==id){
                return game;
            }
        }
        return new PingPongGame();
    }

    public String writeGameToJson(PingPongGame game) {
        ObjectMapper mapper = new ObjectMapper();
        List<PingPongGame> games = this.getGames();
        games.add(game);

        try {
            return mapper.writeValueAsString(games);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public String writeGameToJson(List<PingPongGame> games) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(games);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public List<PingPongGame> getGames() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<PingPongGame>> mapType;
        mapType = new TypeReference<List<PingPongGame>>(){};
        try {
            String json = this.readFile();
            if(json.equals("")){
                return new ArrayList<PingPongGame>();
            }
            List<PingPongGame> games = mapper.readValue(this.readFile(), mapType);
            return games;
        } catch(IOException e){
            List<PingPongGame> games = new ArrayList<>();
            e.printStackTrace();
            return games;
        }
    }

    public String readFile() {
        String json="";
        try {
            json = this.getFile().readFile();
            return json;
        } catch(NullPointerException e){
            return json;
        }
    }

    public void editWriteGameToFile(PingPongGame oldGame, PingPongGame newGame) {
        List<PingPongGame> games = this.getGames();
        games.set(games.indexOf(oldGame),newGame);

        this.getFile().writeFile(this.writeGameToJson(games),false);
    }

    public void writeGamesToFile(List<PingPongGame> games) {
        this.getFile().writeFile(this.writeGameToJson(games),false);
    }

    public File getFile() {
        return this.file;
    }

    public List<PingPongGame> getGamesForPlayer(Player player) {
        List<PingPongGame> games = this.getGames();
        List<PingPongGame> gamesForPlayer = new ArrayList<>();

        for(PingPongGame game:games){
            if(game.getPlayer1().equals(player) || game.getPlayer2().equals(player)) {
                gamesForPlayer.add(game);
            }
        }
        return gamesForPlayer;
    }


}
