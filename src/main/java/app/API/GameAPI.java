package app.API;

import app.Exceptions.InvalidParameterException;
import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.PersistenceModel.PersistenceGame;
import app.ViewModel.PingPongGame;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin()
@RestController
public class GameAPI {

    @CrossOrigin
    @RequestMapping(method=POST,path="/CreateGame")
    public APIResult createGame(@RequestParam(value="player1ID",required = true) String player1ID,
            @RequestParam(value="player2ID",required = true) String player2ID,
            @RequestParam(value="score1",required = true) String score1,
            @RequestParam(value="score2",required = true) String score2,
            @RequestParam(value="time", required=true) String time) {
        GamePersistenceManager gPM = new GamePersistenceManager();

        try {
            int IDplayer1 = Integer.parseInt(player1ID);
            int IDplayer2 = Integer.parseInt(player2ID);

            int player1Score = Integer.parseInt(score1);
            int player2Score = Integer.parseInt(score2);
            Date newTime = new SimpleDateFormat("yyyyMMMdd").parse(time);

            PersistenceGame game = new PersistenceGame(gPM.getNextID(),IDplayer1,IDplayer2,player1Score,player2Score,newTime);
            gPM.writeGameToFile(game);
            return  new APIResult(true,"Game Created");

        } catch (NumberFormatException | ParseException e ){
            e.printStackTrace();
            return new APIResult(false,"Game Unsuccessfully Created");
        } catch (InvalidParameterException e) {
            return new APIResult(false,e.getMessage());
        }

    }

    @CrossOrigin()
    @RequestMapping(method=POST,path="/EditGame")
    public APIResult editGameNew(@RequestParam(value="iD",required=true) int gameID,
            @RequestParam(value="player1ID",required = false) Optional<Integer> player1ID,
            @RequestParam(value="player2ID",required = false) Optional<Integer> player2ID,
            @RequestParam(value="score1",required = false) Optional<Integer> score1,
            @RequestParam(value="score2",required = false) Optional<Integer> score2,
            @RequestParam(value="time", required = false) Optional<String> time ) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PersistenceGame game = gPM.getGameByIDNew(gameID);
        int newPlayerID1 = game.getPlayer1ID();
        int newPlayerID2 = game.getPlayer2ID();
        int newScore1 = game.getPlayer1Score();
        int newScore2 = game.getPlayer2Score();
        Date newTime = game.getTime();

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

            if (time.isPresent()) {
                try {

                    newTime = new SimpleDateFormat("yyyyMMMdd").parse(time.get());
                } catch(ParseException p ){
                    System.out.println(p.getMessage());
                }
            }

            PersistenceGame newGame = new PersistenceGame(game.getiD(), newPlayerID1, newPlayerID2, newScore1,
                    newScore2,newTime);

            gPM.editWriteGameToFileNew(newGame, game);
            return new APIResult(true, "Game Edited");

        }
        return new APIResult(false, "Game Not Edited");

    }

    @CrossOrigin()
    @RequestMapping(path = "/DeleteGame",method=DELETE)
    public APIResult deleteGame(@RequestParam("iD") int iD) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PersistenceGame game = gPM.getGameByIDNew(iD);
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
        List<PingPongGame> games = gPM.getGamesView();
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
