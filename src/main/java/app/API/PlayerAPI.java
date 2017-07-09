package app.API;

import app.StatsEngine.EloRating;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.StatsEngine.Player;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class PlayerAPI {



    @RequestMapping(method=POST,path="/CreatePlayer")
    public APIResult processCreatePerson(@RequestParam(value="username",required=true) String username) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        int id = pPM.getNextID();
        EloRating elo = new EloRating();
        if(pPM.checkUsernameExists(username)){
            return new APIResult(false, "Username Taken");
        }

        Player player = new Player(elo,id,username);
        pPM.writePlayerToFile(player);
        return new APIResult(true, "Player Created with Username "+username);

    }

    @RequestMapping("/GetPlayer")
    public APIResult processGetPlayer(@RequestParam(value="id", required=true) int id) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayerByID(id);
        if(player.getiD()==0){
            return new APIResult(false,"Player with ID "+id+" not found");
        }
        return new APIResult(true, pPM.writePlayerToJson(player));
    }

    @RequestMapping("EditPlayer")
    public APIResult processEditPlayer(@RequestParam(value="id",required=true) int id,
                                  @RequestParam(value="newUsername",required=true) String newUsername) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        if(pPM.editPlayer(id,newUsername)) {
            return new APIResult(true,"Player Edited");
        }
        return new APIResult(false,"Player Unsuccessfully Edited");
    }

    @RequestMapping("DeletePlayer")
    public APIResult processDeletePlayer(@RequestParam(value="id",required = false) Optional<Integer> id,
                                         @RequestParam(value="username",required = true) String username) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        boolean result = false;
        if(id.isPresent()) {
            result = pPM.deletePlayer(id.get());
            if(result) {
                return new APIResult(true,"Player with ID "+id+" was Deleted");
            }
        } else {
            result = pPM.deletePlayer(username);
            if(result) {
                return new APIResult(true,"Player with username "+ username+" was Deleted");
            }
        }

            return new APIResult(false,"Player was not found");
    }




}