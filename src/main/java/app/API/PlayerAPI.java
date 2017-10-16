package app.API;

import app.Exceptions.InvalidParameterException;
import app.PersistenceManagers.EloRatingPersistenceManager;
import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceModel.PersistenceEloRating;
import app.PersistenceModel.PersistencePlayer;
import app.ViewModel.EloRating;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import app.StatsEngine.SinglePlayerStatisticsCalculator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin()
@RestController
public class PlayerAPI {

    @CrossOrigin
    @RequestMapping(method=POST,path="/CreatePlayer")
    public APIResult createPlayer(@RequestParam(value="username",required=true) String username) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        try {
            pPM.createPlayer(username);
        } catch (IllegalArgumentException e) {
            return new APIResult(false, e.getMessage());
        }

        return new APIResult(true, "Player Created with Username "+username);
    }

    @CrossOrigin
    @RequestMapping("/GetPlayer")
    public APIResult getPlayer(@RequestParam(value="id", required=true) int id) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        GamePersistenceManager gPM = new GamePersistenceManager();
        int gameId = gPM.getGamesNew().get(gPM.getGamesNew().size()-1).getiD();
        Player player = pPM.getViewPlayerByID(id,gameId);
        if(player.getiD()==0){
            return new APIResult(false,"Player with ID "+id+" not found");
        }
        System.out.println("GetPlayer called");
        return new APIResult(true, pPM.writePlayerToJson(player));
    }

    @CrossOrigin
    @RequestMapping("/GetPlayerByUsername")
    public APIResult getPlayerByUsername(@RequestParam(value="username", required=true) String username) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        GamePersistenceManager gPM = new GamePersistenceManager();
        int gameId = gPM.getGamesNew().get(gPM.getGamesNew().size()-1).getiD();
        Player player = pPM.getViewPlayerByUsername(username,gameId);
        if(player.getiD()==0){
            return new APIResult(false,"Player with ID "+username+" not found");
        }
        System.out.println("GetPlayer called");
        return new APIResult(true, pPM.writePlayerToJson(player));
    }

    @CrossOrigin
    @RequestMapping(path="/EditPlayerOld", method=POST)
    public APIResult editPlayerOld(@RequestParam(value="id",required=true) int id,
                                @RequestParam(value="newUsername",required=true) String newUsername) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        if(pPM.updatePlayer(id,newUsername)) {
            return new APIResult(true,"Player Edited");
        }
        return new APIResult(false,"Player Unsuccessfully Edited");
    }


    @CrossOrigin
    @RequestMapping(path="/EditPlayer", method=POST)
    public APIResult editPlayer(@RequestParam(value="id",required=true) int id,
            @RequestParam(value="newUsername",required=true) String newUsername) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        try {
            if (pPM.updatePlayer(id, newUsername)) {
                return new APIResult(true, "Player Edited");
            }
        } catch (IllegalArgumentException e) {
            return new APIResult(false, "Player Unsuccessfully Edited. "+e.getMessage());
        }
        return new APIResult(false, "Player Unsuccessfully Edited.");
    }

    @CrossOrigin
    @RequestMapping(path="/DeletePlayer",method=DELETE)
    public APIResult deletePlayer(@RequestParam(value="id",required = false) Optional<Integer> id,
            @RequestParam(value="username",required = false) String username) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        boolean result = false;
        if(id.isPresent()) {
            result = pPM.deletePlayerById(id.get());
            if(result) {
                return new APIResult(true,"Player with ID "+id.get()+" was Deleted");
            }
        } else {
            result = pPM.deletePlayerByUsername(username);
            if(result) {
                return new APIResult(true,"Player with newUsername "+ username+" was Deleted");
            }
        }

        return new APIResult(false,"Player was not found");
    }



    @RequestMapping("/GetPlayerWithHighestRating")
    public APIResult getPlayerWithHighestRating() {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        return new APIResult(true,pPM.writePlayerToJson(pPM.getPlayerWithHighestRating()));
    }

    @CrossOrigin
    @RequestMapping(path = "/GetPlayers",method=GET)
    public APIResult getPlayers() {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        List<Player> players = pPM.getViewPlayers();
        return new APIResult(true,pPM.writePlayerArrayToJson(players));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(path = "/GetAverageScore",method=GET)
    public APIResult getPlayerAverageScore(@RequestParam(value="id") int id) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> gamesForPlayer = gPM.getGamesForPlayer(gPM.getPlayer(id));
        Player player = gPM.getPlayer(id);
        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(gamesForPlayer,player);

        return new APIResult(true,"[{ \"averageScore\" : \""+calc.getAverageScore()+"\"}]");
    }

    @CrossOrigin
    @RequestMapping(path = "HardDeletePlayer", method=DELETE)
    public APIResult hardDeletePlayer(@RequestParam(value="id",required = false) Optional<Integer> id,
            @RequestParam(value="username",required = false) String username) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        boolean result = false;
        if (id.isPresent()) {
            result = pPM.hardDeletePlayerById(id.get());
            if (result) {
                return new APIResult(true, "Player with ID " + id.get() + " was Deleted");
            }
        } else {
            result = pPM.hardDeletePlayerByUsername(username);
            if (result) {
                return new APIResult(true, "Player with newUsername " + username + " was Deleted");
            }
        }

        return new APIResult(false, "Player was not found");
    }
}