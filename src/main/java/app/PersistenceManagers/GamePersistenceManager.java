package app.PersistenceManagers;


import app.PingPongModel.PingPongGame;
import app.PingPongModel.Player;
import app.ReadWriteFile.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamePersistenceManager {

    private static String FILE_PATH = "pingpong.txt";

    private final File file;

    public GamePersistenceManager() {
        this.file = new File(FILE_PATH);
    }

    public GamePersistenceManager(String filePath) {
        this.file = new File(filePath);
    }

    public void writeGameToFile(PingPongGame game) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        List<PingPongGame> games = this.getGames();
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();


        pPM.updatePlayersEloRating(player1,player2,game);

        this.getFile().writeFile(this.writeCurrentGameToGamesJson(game),false);
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

    public String writeCurrentGameToGamesJson(PingPongGame game) {
        ObjectMapper mapper = new ObjectMapper();
        List<PingPongGame> games = this.getGames();
        games.add(game);

        try {
            return mapper.writeValueAsString(games);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public String writeGameToJson(PingPongGame game) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(game);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public String writeGamesToJson(List<PingPongGame> games) {
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
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        List<Player> playersPriorToGame = pPM.getPlayersPriorToAGame(oldGame);

        pPM.writePlayersToFile(playersPriorToGame);

        Player player1PriorToGame = pPM.getPlayerByID(newGame.getPlayer1().getiD());
        Player player2PriorToGame = pPM.getPlayerByID(newGame.getPlayer2().getiD());

        games.set(games.indexOf(oldGame),newGame);

        List<PingPongGame> copyOfGames = new ArrayList<>(games);

        for(int i = games.indexOf(newGame); i< games.size(); i++){
            List<Player> currentPlayers = pPM.getPlayers();
            if(i>games.indexOf(newGame)){
                PingPongGame gameToEdit = games.get(i);
                //replace players with new rating before updating again
                PingPongGame editedGame = new PingPongGame(oldGame.getiD(),
                        pPM.getPlayerByID(oldGame.getPlayer1().getiD()),
                        pPM.getPlayerByID(oldGame.getPlayer2().getiD()),
                        oldGame.getPlayer1Score(),oldGame.getPlayer2Score());
                copyOfGames.set(i,editedGame);
            }
            pPM.updatePlayersEloRating(player1PriorToGame,player2PriorToGame,copyOfGames.get(i));

        }
        this.writeGamesToFile(copyOfGames);
    }

    public void editWriteGameToFile(PingPongGame game, Player player1, Player player2, int score1, int score2) {
        List<PingPongGame> games = this.getGames();
        List<PingPongGame> gamesPrior = games.subList(0,games.indexOf(game));
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        //get the last instance of the player prior to the game being edited
        for(int i = 0; i< gamesPrior.size(); i++) {
            if(gamesPrior.get(i).getPlayer1().getiD()==player1.getiD()) {
                player1 = gamesPrior.get(i).getPlayer1();
            }
            if(gamesPrior.get(i).getPlayer2().getiD()==player2.getiD()) {
                player2 = gamesPrior.get(i).getPlayer2();
            }
        }
        //updated the player persistence layer to be up to date with these two players
        pPM.writePlayerToFile(player1);
        pPM.writePlayerToFile(player2);
        PingPongGame updatedGame = new PingPongGame(game.getiD(),player1, player2,score1, score2);
        this.writeGameToFile(updatedGame);
    }

    public void writeGamesToFile(List<PingPongGame> games) {
        this.getFile().writeFile(this.writeGamesToJson(games),false);
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

    public void editPlayerUsername(Player newPlayer) {
        List<PingPongGame> games = this.getGames();
        Player player1;
        Player player2;
        for(PingPongGame game:games) {
            if(game.getPlayer1().getiD() == newPlayer.getiD()) {
                player1 = new Player(game.getPlayer1().getEloRating(),game.getPlayer1().getiD(),newPlayer.getUsername());
                player2 = game.getPlayer2();
                PingPongGame newGame = new PingPongGame(game.getiD(),player1,player2,game.getPlayer1Score(),game.getPlayer2Score());
                games.set(games.indexOf(game),newGame);
            } else if(game.getPlayer2().getiD() == newPlayer.getiD()) {
                player2 = new Player(game.getPlayer2().getEloRating(),game.getPlayer2().getiD(),newPlayer.getUsername());
                player1 = game.getPlayer1();
                PingPongGame newGame = new PingPongGame(game.getiD(),player1,player2,game.getPlayer1Score(),game.getPlayer2Score());
                games.set(games.indexOf(game),newGame);
            }
        }
        this.writeGamesToFile(games);
    }
}
