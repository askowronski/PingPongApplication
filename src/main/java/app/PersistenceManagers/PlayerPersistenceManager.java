package app.PersistenceManagers;

import app.PersistenceModel.PersistenceEloRating;
import app.PersistenceModel.PersistenceGame;
import app.PersistenceModel.PersistencePlayer;
import app.PersistenceModel.PersistencePlayerEloRatingList;
import app.ViewModel.EloRating;
import app.ViewModel.GameOutcomeEnum;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import app.ReadWriteFile.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PlayerPersistenceManager {

    public static String FILE_PATH = "pingPongPlayers.txt";

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

    public void writePlayerToFile(PersistencePlayer player) {
        try{
            List<PersistencePlayer> currentPlayers = this.readPlayersFromJson(this.getFile().readFile());
            currentPlayers.add(player);
            this.getFile().writeFile(this.writePlayersToJson(currentPlayers),false);
        } catch(NullPointerException e){
            List<PersistencePlayer> players = new ArrayList<>();
            players.add(player);
            this.getFile().writeFile(this.writePlayersToJson(players),false);
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

    public int getNextIDOld() {
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

    public int getNextID() {
        String json = this.readFile();
        if(json.equals("No Players Found")){
            return 1;
        }
        List<PersistencePlayer> players = this.readPlayersFromJson(json);

        List<Integer> ids = new ArrayList<>();
        int highestID = 0;
        for(PersistencePlayer player:players){
            if(player.getId()>highestID){
                highestID=player.getId();
            }
        }
        return highestID+1;
    }

    public boolean checkUsernameExists(String username) {
        List<Player> players = this.getPlayersOld();
        List<String> usernames = new ArrayList<>();

        for(Player player:players) {
            usernames.add(player.getUsername());
        }
        return usernames.contains(username);
    }

    public boolean checkUsernameExistsNew(String username) {
        List<PersistencePlayer> players = this.getPlayersNew();
        List<String> usernames = new ArrayList<>();

        for(PersistencePlayer player:players) {
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

    public String writePlayersToJson(List<PersistencePlayer> players) {
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

    public List<PersistencePlayer> readPlayersFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<PersistencePlayer>> mapType;
        mapType = new TypeReference<List<PersistencePlayer>>(){};
        try {
            List<PersistencePlayer> players = mapper.readValue(json, mapType);
            return players;
        } catch(IOException e){
            List<PersistencePlayer> players = new ArrayList<>();
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

    public Player getPlayerByIDOld(int id){
        List<Player> players = this.readPlayerArrayFromJson(this.getFile().readFile());
        for(Player player:players){
            if(player.getiD()==id){
                return player;
            }
        }
        return new Player(new EloRating(0),0,"Player Does Not Exist");
    }

    public PersistencePlayer getPlayerByIDNew(int id){
        List<PersistencePlayer> players = this.readPlayersFromJson(this.getFile().readFile());
        for(PersistencePlayer player:players){
            if(player.getId()==id){
                return player;
            }
        }
        return new PersistencePlayer(0,"Player Does Not Exist");
    }

    public Player getPlayerByUsernameOld(String username){
        List<Player> players = this.readPlayerArrayFromJson(this.getFile().readFile());
        for(Player player:players){
            if(player.getUsername().equals(username)){
                return player;
            }
        }
        return new Player(new EloRating(0),0,"Player Does Not Exist");
    }

    public PersistencePlayer getPlayerByUsernameNew(String username){
        List<PersistencePlayer> players = this.readPlayersFromJson(this.getFile().readFile());
        for(PersistencePlayer player:players){
            if(player.getUsername().equals(username)){
                return player;
            }
        }
        return new PersistencePlayer(0,"Player Does Not Exist");
    }

    public List<Player> getPlayersOld() {
        String json = this.readFile();
        List<Player> players = new ArrayList<>();
        if(json.equals("No Players Found")){
            return players;
        }
        return this.readPlayerArrayFromJson(json);
    }

    public List<PersistencePlayer>  getPlayersNew() {
        String json = this.readFile();
        List<PersistencePlayer> players = new ArrayList<>();
        if(json.equals("No Players Found")){
            return players;
        }

        List<PersistencePlayer> allPlayers = this.readPlayersFromJson(json);

        for(PersistencePlayer player:allPlayers) {
            if(player.getDeleted() == 0){
                players.add(player);
            }
        }
        return players;
    }


    public boolean editPlayer(int id,String newUsername) {
        Player player = this.getPlayerByIDOld(id);

        if(player.getiD()==0) {
            return false;
        }

        Player newPlayer = new Player(player.getEloRating(),id,newUsername);

        List<Player> players = this.getPlayersOld();

        players.set(players.indexOf(player),newPlayer);

        GamePersistenceManager gPM = new GamePersistenceManager();
        gPM.editPlayerUsername(newPlayer);

        this.getFile().writeFile(this.writePlayerArrayToJson(players),false);
        return true;

    }

    public boolean editPlayerNew(int id,String newUsername) {
        PersistencePlayer player = this.getPlayerByIDNew(id);

        if(player.getId()==0 || this.checkUsernameExistsNew(newUsername)) {
            return false;
        }

        PersistencePlayer newPlayer = new PersistencePlayer(id,newUsername);

        List<PersistencePlayer> players = this.getPlayersNew();

        players.set(players.indexOf(player),newPlayer);

        GamePersistenceManager gPM = new GamePersistenceManager();

        this.getFile().writeFile(this.writePlayersToJson(players),false);
        return true;
    }

    public boolean deletePlayerOld(int id) {
        Player player = this.getPlayerByIDOld(id);

        if(player.getiD()==0) {
            return false;
        }

        List<Player> players = this.getPlayersOld();

        players.remove(player);

        this.getFile().writeFile(this.writePlayerArrayToJson(players),false);
        return true;

    }

    public boolean deletePlayerNew(int id) {
        PersistencePlayer player = this.getPlayerByIDNew(id);

        if(player.getId()==0) {
            return false;
        }

        List<PersistencePlayer> players = this.getPlayersNew();
        int indexOfPlayer = players.indexOf(player);
        player.softDeletePlayer();

        players.set(indexOfPlayer,player);

        this.getFile().writeFile(this.writePlayersToJson(players),false);
        return true;

    }



    public boolean deletePlayerOld(String username) {
        Player player = this.getPlayerByUsernameOld(username);

        if(player.getiD()==0) {
            return false;
        }

        List<Player> players = this.getPlayersOld();

        players.remove(player);

        this.getFile().writeFile(this.writePlayerArrayToJson(players),false);
        return true;

    }

    public boolean deletePlayerNew(String username) {
        PersistencePlayer player = this.getPlayerByUsernameNew(username);

        if(player.getId()==0) {
            return false;
        }

        List<PersistencePlayer> players = this.getPlayersNew();

        int indexOfPlayer = players.indexOf(player);
        player.softDeletePlayer();

        players.set(indexOfPlayer,player);

        this.getFile().writeFile(this.writePlayersToJson(players),false);
        return true;

    }

    public Player getPlayerWithHighestRating() {
        List<Player> players = this.getPlayersOld();
        Player highestPlayer = players.get(0);
        for(int i = 0; i< players.size(); i++) {
            if(players.get(i).getEloRating().getRating()>highestPlayer.getEloRating().getRating()){
                highestPlayer= players.get(i);
            }
        }
        return highestPlayer;
    }

    public void updatePlayersEloRatingOld(Player player1PriorToGame, Player player2PriorToGame,PingPongGame game) {
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

    public void updatePlayersEloRatingNew(PersistenceGame game) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PersistenceGame> games = gPM.getGamesNew();

        int indexOfGame = games.indexOf(game);

        for(int i = indexOfGame; i < games.size(); i++){
            EloRatingPersistenceManager eRPM1 = new EloRatingPersistenceManager(games.get(i).getPlayer1ID());
            EloRatingPersistenceManager eRPM2 = new EloRatingPersistenceManager(games.get(i).getPlayer2ID());
            PersistencePlayerEloRatingList listPlayer1 = eRPM1.getEloRatingList();
            PersistencePlayerEloRatingList listPlayer2 = eRPM2.getEloRatingList();
            PersistenceGame currentGame = games.get(i);
            int indexOfGame1 = listPlayer1.getIndexOfGame(currentGame.getiD());
            int indexOfGame2 = listPlayer2.getIndexOfGame(currentGame.getiD());
            PersistenceEloRating newRating1;
            PersistenceEloRating newRating2;
            int indexOfElo1;
            int indexOfElo2;
            if(indexOfGame1 <0){
                indexOfElo1 = listPlayer1.getListSize()-1;
            } else {
                indexOfElo1 = indexOfGame1-1;
            }
            if(indexOfGame2 <0){
                indexOfElo2=listPlayer2.getListSize()-1;
            } else {
                indexOfElo2=indexOfGame2-1;
            }


            if(currentGame.getPlayer1Score() > currentGame.getPlayer2Score()) {
                newRating1 = listPlayer1.getRating(indexOfElo1).
                        newRating(GameOutcomeEnum.WIN,listPlayer2.getRating(indexOfElo2));
                newRating2 = listPlayer2.getRating(indexOfElo2).
                        newRating(GameOutcomeEnum.LOSS,listPlayer1.getRating(indexOfElo1));
            } else if(currentGame.getPlayer1Score() < currentGame.getPlayer2Score()) {
                newRating1 = listPlayer1.getRating(indexOfElo1).
                        newRating(GameOutcomeEnum.LOSS,listPlayer2.getRating(indexOfElo2));
                newRating2 = listPlayer2.getRating(indexOfElo2).
                        newRating(GameOutcomeEnum.WIN,listPlayer1.getRating(indexOfElo1));
            } else {
                newRating1 = listPlayer1.getRating(indexOfElo1).
                        newRating(GameOutcomeEnum.DRAW,listPlayer2.getRating(indexOfElo2));
                newRating2 = listPlayer2.getRating(indexOfElo2).
                        newRating(GameOutcomeEnum.DRAW,listPlayer1.getRating(indexOfElo1));

            }
            if(indexOfGame1<0) {
                listPlayer1.addEloRating(newRating1);
            } else {
                listPlayer1.replaceEloRating(indexOfGame1,newRating1);
            }
            if(indexOfGame2 <0) {
                listPlayer2.addEloRating(newRating2);
            } else {
                listPlayer2.replaceEloRating(indexOfGame2, newRating2);
            }


            eRPM1.getFile().writeFile(eRPM1.writeEloRatingListToJson(listPlayer1),false);
            eRPM2.getFile().writeFile(eRPM2.writeEloRatingListToJson(listPlayer2),false);
        }


    }

    public List<Player> getPlayersPriorToAGame(PingPongGame game) {
        List<Player> currentPlayers = this.getPlayersOld();
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> currentGames = gPM.getGamesOld();
        if(!currentGames.contains(game)){
            return currentPlayers;
        }
        List<PingPongGame> splicedGames = currentGames.subList(currentGames.indexOf(game)+1,currentGames.size());

        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();



        for(int i = 0; i < currentPlayers.size(); i++) {
            Player playerCheck = currentPlayers.get(i);

            if(playerCheck.getiD() == player1.getiD() || playerCheck.getiD() == player2.getiD()){
                break;
            }
            for(int j = 0; j < splicedGames.size(); j++ ){
                if(splicedGames.get(j).getPlayer1().getiD() == playerCheck.getiD()) {
                    currentPlayers.set(i,splicedGames.get(j).getPlayer1());
                    break;
                }
                if(splicedGames.get(j).getPlayer2().getiD() == playerCheck.getiD()) {
                    currentPlayers.set(i,splicedGames.get(j).getPlayer2());
                    break;
                }
//                if(j==0){
//                    Player newPlayer = new Player(new EloRating(),currentPlayers.get(i).getiD(),currentPlayers.get(i).getUsername());
//                    currentPlayers.set(i,newPlayer);
//                }
            }
        }
        return currentPlayers;
    }
}
