package app.PersistenceManagers;


import app.PersistenceModel.PersistenceGame;
import app.PersistenceModel.PersistencePlayerEloRatingList;
import app.ViewModel.EloRating;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import app.ReadWriteFile.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GamePersistenceManager {

    private static String FILE_PATH = "pingPongGames.txt";

    private final File file;

    public GamePersistenceManager() {
        this.file = new File(FILE_PATH);
    }

    public GamePersistenceManager(String filePath) {
        this.file = new File(filePath);
    }

    public void writeGameToFileOld(PingPongGame game) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        List<PingPongGame> games = this.getGamesOld();
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();


        pPM.updatePlayersEloRatingOld(player1,player2,game);

        this.getFile().writeFile(this.writeCurrentGameToGamesJsonOld(game),false);
    }

    public void writeGameToFileNew(PersistenceGame game) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        List<PersistenceGame> games = this.getGamesNew();

        games.add(game);

        this.getFile().writeFile(this.writeCurrentGameToGamesJsonNew(game),false);
        pPM.updatePlayersEloRatingNew(game);

    }

    public Player getPlayer(int id){
        PlayerPersistenceManager ppm = new PlayerPersistenceManager();
        return ppm.getPlayerByIDOld(id);
    }

    public int getNextID() {
        String json = this.readFile();
        if(json.equals("")){
            return 1;
        }
        List<PingPongGame> games = this.getGamesOld();

        List<Integer> ids = new ArrayList<>();
        int highestID = 0;
        for(PingPongGame game:games){
            if(game.getiD()>highestID){
                highestID=game.getiD();
            }
        }
        return highestID+1;
    }

    public PingPongGame getGameByIDOld(int id) {
        List<PingPongGame> games = this.getGamesOld();
        for(PingPongGame game:games){
            if(game.getiD()==id){
                return game;
            }
        }
        return new PingPongGame();
    }

    public PersistenceGame getGameByIDNew(int id) {
        List<PersistenceGame> games = this.getGamesNew();
        for(PersistenceGame game:games){
            if(game.getiD()==id){
                return game;
            }
        }
        return new PersistenceGame();
    }



    public String writeCurrentGameToGamesJsonOld(PingPongGame game) {
        ObjectMapper mapper = new ObjectMapper();
        List<PingPongGame> games = this.getGamesOld();
        games.add(game);

        try {
            return mapper.writeValueAsString(games);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public String writeCurrentGameToGamesJsonNew(PersistenceGame game) {
        ObjectMapper mapper = new ObjectMapper();
        List<PersistenceGame> games = this.getGamesNew();
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

    public String writeGamesToJsonOld(List<PingPongGame> games) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(games);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public String writeGamesToJsonNew(List<PersistenceGame> games) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(games);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public List<PingPongGame> getGamesOld() {
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

    public List<PersistenceGame> getGamesNew() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<PersistenceGame>> mapType;
        mapType = new TypeReference<List<PersistenceGame>>(){};
        try {
            String json = this.readFile();
            if(json.equals("")){
                return new ArrayList<PersistenceGame>();
            }
            List<PersistenceGame> games = mapper.readValue(this.readFile(), mapType);
            return games;
        } catch(IOException e){
            List<PersistenceGame> games = new ArrayList<>();
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
        List<PingPongGame> games = this.getGamesOld();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        List<Player> playersPriorToGame = pPM.getPlayersPriorToAGame(oldGame);

        pPM.writePlayersToFile(playersPriorToGame);


        if(newGame == null){
            games.remove(oldGame);
        } else {
            games.set(games.indexOf(oldGame), newGame);
        }

        List<PingPongGame> copyOfGames = new ArrayList<>(games);

        for(int i = games.indexOf(newGame); i< games.size(); i++){

            PingPongGame gameToEdit = games.get(i);
            Player  player1PriorToGame = pPM.getPlayerByIDOld(gameToEdit.getPlayer1().getiD());
            Player player2PriorToGame = pPM.getPlayerByIDOld(gameToEdit.getPlayer2().getiD());
            //replace players with new rating before updating again
            PingPongGame editedGame = new PingPongGame(gameToEdit.getiD(),
                    player1PriorToGame,
                    player2PriorToGame,
                    gameToEdit.getPlayer1Score(),gameToEdit.getPlayer2Score());
            copyOfGames.set(i,editedGame);
            pPM.updatePlayersEloRatingOld(player1PriorToGame,player2PriorToGame,copyOfGames.get(i));
        }
        this.writeGamesToFileOld(copyOfGames);
    }

    public void editWriteGameToFileNew(PersistenceGame newGame) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        List<PersistenceGame> games = this.getGamesNew();

        for(int i = 0; i< games.size(); i++){
            if(games.get(i).getiD() == newGame.getiD()){
                games.set(i,newGame);
                break;
            }
        }

        this.writeGamesToFileNew(games);

        pPM.updatePlayersEloRatingNew(newGame);
    }

    public void deleteGameWriteToFile(PingPongGame game){
        this.editWriteGameToFile(game,null);
    }

    public void editWriteGameToFile(PingPongGame game, Player player1, Player player2, int score1, int score2) {
        List<PingPongGame> games = this.getGamesOld();
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
        this.writeGameToFileOld(updatedGame);
    }

    public void writeGamesToFileOld(List<PingPongGame> games) {
        this.getFile().writeFile(this.writeGamesToJsonOld(games),false);
    }

    public void writeGamesToFileNew(List<PersistenceGame> games) {
        this.getFile().writeFile(this.writeGamesToJsonNew(games),false);
    }

    public File getFile() {
        return this.file;
    }

    public List<PingPongGame> getGamesForPlayer(Player player) {
        List<PingPongGame> games = this.getGamesOld();
        List<PingPongGame> gamesForPlayer = new ArrayList<>();

        for(PingPongGame game:games){
            if(game.getPlayer1().equals(player) || game.getPlayer2().equals(player)) {
                gamesForPlayer.add(game);
            }
        }
        return gamesForPlayer;
    }

    public void editPlayerUsername(Player newPlayer) {
        List<PingPongGame> games = this.getGamesOld();
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
        this.writeGamesToFileOld(games);
    }

    public Player getViewPlayerPriorToGame(int playerId,int gameID){
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(playerId);
        PersistencePlayerEloRatingList ratings = eRPM.getEloRatingList();
        int indexOfRatingPrior = ratings.getIndexOfGame(gameID)-1;

        return new Player(new EloRating(ratings.getRating(indexOfRatingPrior).getEloRating()),
                pPM.getPlayerByIDNew(playerId).getId(),pPM.getPlayerByIDNew(playerId).getUsername());
    }
}
