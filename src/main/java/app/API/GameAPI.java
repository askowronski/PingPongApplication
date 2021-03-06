package app.API;

import app.Exceptions.InvalidParameterException;
import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.PersistenceModel.PersistenceGame;
import app.ViewModel.DateRange;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import com.mysql.cj.core.util.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import javafx.util.*;
import javax.persistence.NoResultException;
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
            @RequestParam(value="date", required=true) String date,
            @RequestParam(value="time", required=false) Optional<String> time) {
        GamePersistenceManager gPM = new GamePersistenceManager();

        try {
            if (StringUtils.isEmptyOrWhitespaceOnly(player1ID) ||
                    StringUtils.isEmptyOrWhitespaceOnly(player2ID)) {
                return new APIResult(false,"Game Unsuccessfully Created. Provide two players.");
            }

            int IDplayer1 = Integer.parseInt(player1ID);
            int IDplayer2 = Integer.parseInt(player2ID);

            int player1Score = Integer.parseInt(score1);
            int player2Score = Integer.parseInt(score2);
            Date newTime;
            if (!time.isPresent()) {
                newTime = new SimpleDateFormat("yyyyMMMdd").parse(date);
            } else {
                newTime = new SimpleDateFormat("yyyyMMMdd H:mm").parse(date+" "+time.get());
            }

            if (gPM.doesPlayerHaveFourGamesOnDate(newTime, IDplayer1) ||
                    gPM.doesPlayerHaveFourGamesOnDate(newTime, IDplayer2)) {
                return new APIResult(false, "Should you be doing that?");
            }

            PersistenceGame game = new PersistenceGame(gPM.getNextID(),IDplayer1,IDplayer2,player1Score,player2Score,newTime);
            gPM.createGame(game);
            PingPongGame viewGame = gPM.getViewGameByID(game.getiD());
            return  new ApiResultWithGame(true,"Game Created", viewGame.toJson());

        } catch (NumberFormatException e){
            e.printStackTrace();
            return new APIResult(false,"Game Unsuccessfully Created. Provide numbers for scores.");
        }
        catch (ParseException e){
            e.printStackTrace();
            return new APIResult(false,"Game Unsuccessfully Created. Provide a valid date");
        }catch (InvalidParameterException e) {
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
        PersistenceGame game = gPM.getGameByID(gameID);
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
                    newTime = new SimpleDateFormat("yyyyMMMdd H:mm").parse(time.get());
                } catch(ParseException p ){
                    System.out.println(p.getMessage());
                    return new APIResult(false, "Provide a valid date.");
                }
            }

            PersistenceGame newGame = new PersistenceGame(game.getiD(), newPlayerID1, newPlayerID2, newScore1,
                    newScore2,newTime);
            try {
                gPM.editWriteGameToFileNew(newGame, game);
                return new APIResult(true, "Game Edited");
            } catch (IllegalArgumentException | NoResultException | InvalidParameterException e) {
                return new APIResult(false, e.getMessage());
            }

        }
        return new APIResult(false, "Game Not Edited");

    }

    @CrossOrigin()
    @RequestMapping(path = "/DeleteGame",method=DELETE)
    public APIResult deleteGame(@RequestParam("iD") int iD) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PersistenceGame game = gPM.getGameByID(iD);
        if(game.getiD()==0){
            return new APIResult(false,"Game Not Found");
        }

        gPM.deleteGamePropogate(game);
        return new APIResult(true,"Game Deleted");
    }

    @CrossOrigin()
    @RequestMapping(path = "/HardDeleteGame",method=DELETE)
    public APIResult hardDeleteGame(@RequestParam("iD") int iD) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PersistenceGame game = gPM.getGameByID(iD);
        if(game.getiD()==0){
            return new APIResult(false,"Game Not Found");
        }

        gPM.hardDeleteGame(game);
        return new APIResult(true,"Game Deleted");
    }

    @CrossOrigin()
    @RequestMapping(path = "/GetGame", method=GET)
    public APIResult getGame(@RequestParam("iD") int iD) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        try {
            PingPongGame game = gPM.getViewGameByID(iD);
            return new APIResult(true, gPM.writeGameToJson(game));
        } catch (NoSuchElementException e) {
            return new APIResult(false, e.getMessage());
        }

    }

    @CrossOrigin()
    @RequestMapping(path = "/GetLastGame", method=GET)
    public APIResult getLastGame() {
        GamePersistenceManager gPM = new GamePersistenceManager();
        try {
            PingPongGame game = gPM.getLastGame();
            return new APIResult(true, gPM.writeGameToJson(game));
        } catch (NoSuchElementException | NoResultException e) {
            return new APIResult(false, e.getMessage());
        }

    }

    @CrossOrigin
    @RequestMapping(path = "/GetGames", method=GET)
    public APIResult getGames() {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> games = gPM.getGamesView();
        return new APIResult(true, gPM.writeViewGamesToJson(games));

    }

    @CrossOrigin
    @RequestMapping(path = "/GetGamesNoRatings", method=GET)
    public APIResult getGamesNoRatings() {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> games = gPM.getGamesViewWithoutEloRatings();
        return new APIResult(true, gPM.writeViewGamesToJson(games));

    }



    @CrossOrigin
    @RequestMapping(path = "/GetGamesForPlayer", method=GET)
    public APIResult getGamesForPlayer(@RequestParam(value="id") int id) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getViewPlayerByID(id, 0);
        List<PingPongGame> games = gPM.getGamesForPlayer(player);
        return new APIResult(true, gPM.writeViewGamesToJson(games));
    }

    @CrossOrigin
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

    @CrossOrigin
    @RequestMapping(path = "/GetDateRangeForPlayersGames", method=GET)
    public APIResult getDateRangeForPlayersGame(@RequestParam(value="id") int playerId) {

        GamePersistenceManager gPM = new GamePersistenceManager();
        DateRange dates = gPM.getDateRangeOfGamesForPlayer(gPM.getPersistenceGamesForPlayer(playerId));
        return new ApiResultDates(true, dates.getBeginningString(), dates.getEndString());

    }

    @CrossOrigin
    @RequestMapping(path = "/AreThereGames", method=GET)
    public APIResult getAreThereGames() {

        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PersistenceGame> games = gPM.getGamesNew();
        return new APIResult(games.size() > 0, "");

    }



}
