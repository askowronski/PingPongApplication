package app.API;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import app.PersistenceManagers.EloRatingPersistenceManager;
import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.PersistenceModel.PersistencePlayerEloRatingList;
import app.ViewModel.PingPongGame;
import java.util.List;
import javax.persistence.NoResultException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin()
@RestController
public class EloRatingsAPI {

    @CrossOrigin
    @RequestMapping(path = "/GetEloRatingsTest", method=GET)
    public APIResult getGames(@RequestParam(value = "playerId", required =  true) int playerId) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        try {
            pPM.getPlayerById(playerId);
        } catch (NoResultException e) {
            return new APIResult(false, e.getMessage());
        }

        EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(playerId);
        PersistencePlayerEloRatingList list = eRPM.getEloRatingList();

        return new APIResult(true, eRPM.writeEloRatingListToJson(list.getList()));
    }

}
