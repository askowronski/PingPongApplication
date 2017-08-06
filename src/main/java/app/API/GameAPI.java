package app.API;

import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.PingPongModel.PingPongGame;
import app.PingPongModel.Player;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin()
@RestController
public class GameAPI {

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(method=POST,path="/CreateGame")
    public APIResult createGame(@RequestParam(value="player1ID",required = true) String player1ID,
                                @RequestParam(value="player2ID",required = true) String player2ID,
                                @RequestParam(value="score1",required = true) String score1,
                                @RequestParam(value="score2",required = true) String score2) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        try {
            int IDplayer1 = Integer.parseInt(player1ID);
            int IDplayer2 = Integer.parseInt(player2ID);

            int player1Score = Integer.parseInt(score1);
            int player2Score = Integer.parseInt(score2);

            Player player1 = gPM.getPlayer(IDplayer1);
            Player player2 = gPM.getPlayer(IDplayer2);

            PingPongGame game = new PingPongGame(gPM.getNextID(),player1,player2,player1Score,player2Score);
            gPM.writeGameToFile(game);
            pPM.updatePlayersEloRating(player1,player2,game);
            return  new APIResult(true,"Game Created");

        } catch (NumberFormatException e ){
            e.printStackTrace();
            return new APIResult(false,"Game Unsuccessfully Created");
        }

    }

    @CrossOrigin()
    @RequestMapping(method=POST,path="/EditGame")
    public APIResult editGame(@RequestParam(value="iD",required=true) int gameID,
                              @RequestParam(value="player1ID",required = false) Optional<Integer> player1ID,
                              @RequestParam(value="player2ID",required = false) Optional<Integer> player2ID,
                              @RequestParam(value="score1",required = false) Optional<Integer> score1,
                              @RequestParam(value="score2",required = false) Optional<Integer> score2){
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        PingPongGame game = gPM.getGameByID(gameID);
        Player newPlayer1;
        Player newPlayer2;
        int newScore1;
        int newScore2;

        if(game.getiD()!=0){
            if(player1ID.isPresent() && player1ID.get()!=game.getPlayer1().getiD()){
                newPlayer1=pPM.getPlayerByID(player1ID.get());
            } else {
                newPlayer1 = game.getPlayer1();
            }

            if(player2ID.isPresent()&& player2ID.get()!=game.getPlayer2().getiD()){
                newPlayer2=pPM.getPlayerByID(player2ID.get());
            } else {
                newPlayer2 = game.getPlayer2();
            }

            if(score1.isPresent()){
                newScore1=score1.get();
            } else {
                newScore1 = game.getPlayer1Score();
            }

            if(score2.isPresent()){
                newScore2=score2.get();
            } else {
                newScore2 = game.getPlayer2Score();
            }

            PingPongGame newGame = new PingPongGame(game.getiD(),newPlayer1,newPlayer2,newScore1,newScore2);
            gPM.editWriteGameToFile(game,newGame);
            return new APIResult(true,"Game Edited");
        }
            return new APIResult(false,"Game Unsuccessfully Edited");

    }

    @RequestMapping(path = "EditGameJson", method = POST, consumes = "application/json")
    public APIResult editGameJson(@RequestBody PingPongGame game){
        if(game.getiD()>0) {
            GamePersistenceManager gPM = new GamePersistenceManager();
            PlayerPersistenceManager pPM = new PlayerPersistenceManager();
            List<PingPongGame> oldGames = gPM.getGames();
            gPM.editWriteGameToFile(gPM.getGameByID(game.getiD()), game);
            return new APIResult(true,"Game Edited");
        }
        return new APIResult(false,"Game Unsuccessfully Edited");

    }

    @CrossOrigin()
    @RequestMapping(path = "/DeleteGame",method=DELETE)
    public APIResult deleteGame(@RequestParam("iD") int iD) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PingPongGame game = gPM.getGameByID(iD);
        if(game.getiD()==0){
            return new APIResult(false,"Game Not Found");
        }
        gPM.deleteGameWriteToFile(game);
        return new APIResult(true,"Game Deleted");
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(path = "/GetGame", method=GET)
    public APIResult getGame(@RequestParam("iD") int iD) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PingPongGame game = gPM.getGameByID(iD);
        return new APIResult(true, gPM.writeGameToJson(game));

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(path = "/GetGames", method=GET)
    public APIResult getGames() {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> games = gPM.getGames();
        return new APIResult(true, gPM.writeGamesToJson(games));

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(path = "/GetGamesForPlayer", method=GET)
    public APIResult getGamesForPlayer(@RequestParam(value="id") int id) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> games = gPM.getGamesForPlayer(gPM.getPlayer(id));
        return new APIResult(true, gPM.writeGamesToJson(games));

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(path = "/GetGamesForPlayerChart", method=GET)
    public APIResult getGamesForPlayerChart(@RequestParam(value="id") int id) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> games = gPM.getGamesForPlayer(gPM.getPlayer(id));
        String json="[";
        for(PingPongGame game:games){
            json+="{\"name\":\"Game\",\"value\":\"sick\",\"unit\":\"\"},";
        }
        json=json.substring(0,json.length()-1);
        json+="]";
        return new APIResult(true, json);

    }


}
