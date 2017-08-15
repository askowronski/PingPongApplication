package app.API;

import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.PersistenceModel.PersistenceGame;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
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
    @RequestMapping(method=POST,path="/CreateGameOld")
    public APIResult createGameOld(@RequestParam(value="player1ID",required = true) String player1ID,
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
            gPM.writeGameToFileOld(game);
            pPM.updatePlayersEloRatingOld(player1,player2,game);
            return  new APIResult(true,"Game Created");

        } catch (NumberFormatException e ){
            e.printStackTrace();
            return new APIResult(false,"Game Unsuccessfully Created");
        }

    }

    @CrossOrigin
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

            PersistenceGame game = new PersistenceGame(gPM.getNextID(),IDplayer1,IDplayer2,player1Score,player2Score);
            gPM.writeGameToFileNew(game);
            return  new APIResult(true,"Game Created");

        } catch (NumberFormatException e ){
            e.printStackTrace();
            return new APIResult(false,"Game Unsuccessfully Created");
        }

    }

    @CrossOrigin()
    @RequestMapping(method=POST,path="/EditGameOld")
    public APIResult editGameOld(@RequestParam(value="iD",required=true) int gameID,
                              @RequestParam(value="player1ID",required = false) Optional<Integer> player1ID,
                              @RequestParam(value="player2ID",required = false) Optional<Integer> player2ID,
                              @RequestParam(value="score1",required = false) Optional<Integer> score1,
                              @RequestParam(value="score2",required = false) Optional<Integer> score2){
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        PingPongGame game = gPM.getGameByIDOld(gameID);
        Player newPlayer1;
        Player newPlayer2;
        int newScore1;
        int newScore2;

        if(game.getiD()!=0){
            if(player1ID.isPresent() && player1ID.get()!=game.getPlayer1().getiD()){
                newPlayer1=pPM.getPlayerByIDOld(player1ID.get());
            } else {
                newPlayer1 = game.getPlayer1();
            }

            if(player2ID.isPresent()&& player2ID.get()!=game.getPlayer2().getiD()){
                newPlayer2=pPM.getPlayerByIDOld(player2ID.get());
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

    @CrossOrigin()
    @RequestMapping(method=POST,path="/EditGame")
    public APIResult editGameNew(@RequestParam(value="iD",required=true) int gameID,
            @RequestParam(value="player1ID",required = false) Optional<Integer> player1ID,
            @RequestParam(value="player2ID",required = false) Optional<Integer> player2ID,
            @RequestParam(value="score1",required = false) Optional<Integer> score1,
            @RequestParam(value="score2",required = false) Optional<Integer> score2) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        PersistenceGame game = gPM.getGameByIDNew(gameID);
        int newPlayerID1 = game.getPlayer1ID();
        int newPlayerID2 = game.getPlayer2ID();
        int newScore1 = game.getPlayer1Score();
        int newScore2 = game.getPlayer2Score();

        if (game.getiD() != 0) {
            if (player1ID.isPresent()) {
                newPlayerID1 = player1ID.get();
            }

            if (player2ID.isPresent()) {
                newPlayerID2 = player2ID.get();
            }

            if (score1.isPresent()) {
                newScore1 = score1.get();
            }

            if (score2.isPresent()) {
                newScore2 = score2.get();
            }

            PersistenceGame newGame = new PersistenceGame(game.getiD(), newPlayerID1, newPlayerID2, newScore1,
                    newScore2,game.getTime());

            gPM.editWriteGameToFileNew(newGame);
            return new APIResult(true, "Game Edited");

        }
        return new APIResult(false, "Game Not Edited");

    }

    @RequestMapping(path = "EditGameJson", method = POST, consumes = "application/json")
    public APIResult editGameJson(@RequestBody PingPongGame game){
        if(game.getiD()>0) {
            GamePersistenceManager gPM = new GamePersistenceManager();
            PlayerPersistenceManager pPM = new PlayerPersistenceManager();
            List<PingPongGame> oldGames = gPM.getGamesOld();
            gPM.editWriteGameToFile(gPM.getGameByIDOld(game.getiD()), game);
            return new APIResult(true,"Game Edited");
        }
        return new APIResult(false,"Game Unsuccessfully Edited");

    }

    @CrossOrigin()
    @RequestMapping(path = "/DeleteGame",method=DELETE)
    public APIResult deleteGame(@RequestParam("iD") int iD) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PingPongGame game = gPM.getGameByIDOld(iD);
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
        PingPongGame game = gPM.getGameByIDOld(iD);
        return new APIResult(true, gPM.writeGameToJson(game));

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(path = "/GetGames", method=GET)
    public APIResult getGames() {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> games = gPM.getGamesOld();
        return new APIResult(true, gPM.writeGamesToJsonOld(games));

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(path = "/GetGamesForPlayer", method=GET)
    public APIResult getGamesForPlayer(@RequestParam(value="id") int id) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> games = gPM.getGamesForPlayer(gPM.getPlayer(id));
        return new APIResult(true, gPM.writeGamesToJsonOld(games));

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
