package app.PersistenceManagers;


import app.PersistenceModel.PersistenceEloRating;
import app.PersistenceModel.PersistenceGame;
import app.PersistenceModel.PersistencePlayer;
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
import java.util.List;
import java.util.Optional;

public class GamePersistenceManager {

    private static String FILE_PATH = "pingPongGames.txt";

    private final File file;

    public GamePersistenceManager() {
        this.file = new File(FILE_PATH);
    }

    public GamePersistenceManager(String filePath) {
        this.file = new File(filePath);
    }

    public void writeGameToFile(PersistenceGame game) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        List<PersistenceGame> games = this.getGamesNew();

        games.add(game);

        this.getFile().writeFile(this.writeCurrentGameToGamesJsonNew(game),false);
        pPM.updatePlayersEloOnCreateGame(game,this);

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
        List<PingPongGame> games = this.getGamesView();

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
        List<PingPongGame> games = this.getGamesView();
        for(PingPongGame game:games){
            if(game.getiD()==id){
                return game;
            }
        }
        return new PingPongGame();
    }

    public PersistenceGame getGameByIDNew(final int id) {
        List<PersistenceGame> games = this.getGamesNew();

        Optional<PersistenceGame> game = games.stream().filter(g -> g.getiD() == id).findFirst();

        if(game.isPresent()) {
            return game.get();
        } else {
            return new PersistenceGame();
        }
    }



    public String writeCurrentGameToGamesJsonOld(PingPongGame game) {
        ObjectMapper mapper = new ObjectMapper();
        List<PingPongGame> games = this.getGamesView();
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

    public List<PingPongGame> getGamesView() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<PersistenceGame>> mapType;
        mapType = new TypeReference<List<PersistenceGame>>(){};
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        try {
            String json = this.readFile();
            if(json.equals("")){
                return new ArrayList<PingPongGame>();
            }
            List<PersistenceGame> games = mapper.readValue(this.readFile(), mapType);
            List<PingPongGame> viewGames = new ArrayList<>();
            for(PersistenceGame game:games){
                Player player1 = pPM.getViewPlayerByID(game.getPlayer1ID(),game.getiD());
                Player player2 = pPM.getViewPlayerByID(game.getPlayer2ID(),game.getiD());
                viewGames.add(new PingPongGame(game.getiD(),player1,player2,game.getPlayer1Score(),game.getPlayer2Score()));
            }
            return viewGames;
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

    public void editWriteGameToFileNew(PersistenceGame newGame,
            PersistenceGame oldGame) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        List<PersistenceGame> games = this.getGamesNew();

        for(int i = 0; i< games.size(); i++){
            if(games.get(i).getiD() == newGame.getiD()){
                games.set(i,newGame);
                break;
            }
        }

        this.writeGamesToFileNew(games);

        pPM.updatePlayersEloRatingEdit(newGame,oldGame,this);
    }



    public void deleteGameWriteToFile(PingPongGame game){

    }

    public void writeGamesToFileNew(List<PersistenceGame> games) {
        this.getFile().writeFile(this.writeGamesToJsonNew(games),false);
    }

    public File getFile() {
        return this.file;
    }

    public List<PingPongGame> getGamesForPlayer(Player player) {
        List<PingPongGame> games = this.getGamesView();
        List<PingPongGame> gamesForPlayer = new ArrayList<>();

        for(PingPongGame game:games){
            if(game.getPlayer1().getiD() == player.getiD()) {
                gamesForPlayer.add(game);
            } else if (game.getPlayer2().getiD() == player.getiD()) {
                PingPongGame newGame = new PingPongGame(game.getiD(),game.getPlayer2(),
                        game.getPlayer1(),game.getPlayer2Score(),game.getPlayer1Score());
                gamesForPlayer.add(newGame);
            }
        }
        return gamesForPlayer;
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
