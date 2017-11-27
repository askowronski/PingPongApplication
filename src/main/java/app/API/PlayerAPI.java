package app.API;

import app.Exceptions.InvalidParameterException;
import app.PersistenceManagers.EloRatingPersistenceManager;
import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceModel.PersistenceEloRating;
import app.PersistenceModel.PersistenceGame;
import app.PersistenceModel.PersistencePlayer;
import app.ViewModel.EloRating;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import app.StatsEngine.SinglePlayerStatisticsCalculator;
import javax.persistence.NoResultException;
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
    @RequestMapping(method = POST, path = "/CreatePlayer")
    public APIResult createPlayer(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "firstName", required = true) String firstName,
            @RequestParam(value = "lastName", required = true) String lastName) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        try {
            pPM.createPlayer(username, firstName, lastName);
        } catch (IllegalArgumentException e) {
            return new APIResult(false, e.getMessage());
        }

        return new APIResult(true, "Player Created.");
    }

    @CrossOrigin
    @RequestMapping("/GetPlayer")
    public APIResult getPlayer(@RequestParam(value = "id", required = true) int id) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PersistenceGame> gamesForPlayer = gPM.getGamesForPlayer(id);
        int gameId;
        if (gamesForPlayer.size() == 0) {
            gameId = 0;
        } else {
            gameId = gamesForPlayer.get(gamesForPlayer.size() - 1).getiD();
        }
        Player player = pPM.getViewPlayerByID(id, gameId);
        if (player.getiD() == 0) {
            return new APIResult(false, "Player with ID " + id + " not found");
        }
        System.out.println("GetPlayer called");
        return new APIResult(true, pPM.writePlayerToJson(player));
    }

    @CrossOrigin
    @RequestMapping("/GetPlayerByUsername")
    public APIResult getPlayerByUsername(
            @RequestParam(value = "username", required = true) String username) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        GamePersistenceManager gPM = new GamePersistenceManager();
        int gameId;
        PersistencePlayer playerForId = pPM.getPlayerByUsername(username);

        List<PersistenceGame> games = gPM.getGamesForPlayer(playerForId.getId());
        if (games.size() > 0) {
            gameId = games.get(games.size() - 1).getiD();
        } else {
            gameId = 0;
        }
        Player player = pPM.getViewPlayerByUsername(username, gameId);
        if (player.getiD() == 0) {
            return new APIResult(false, "Player with ID " + username + " not found");
        }
        System.out.println("GetPlayer called");
        return new APIResult(true, pPM.writePlayerToJson(player));
    }

    @CrossOrigin
    @RequestMapping(path = "/EditPlayer", method = POST)
    public APIResult editPlayer(@RequestParam(value = "id") int id,
            @RequestParam(value = "newUsername", required = false) String newUsername,
            @RequestParam(value = "newFirstName", required = false) String newFirstName,
            @RequestParam(value = "newLastName", required = false) String newLastName) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        if ((newUsername == null || newUsername.isEmpty())
                && (newFirstName == null || newFirstName.isEmpty())
                && (newLastName == null || newLastName.isEmpty())) {
            return new APIResult(false, "Please supply something to edit.");
        }

        try {
            if (pPM.updatePlayer(id, newUsername, newFirstName, newLastName)) {
                return new APIResult(true, "Player Edited");
            }
        } catch (IllegalArgumentException e) {
            return new APIResult(false, "Player Unsuccessfully Edited. " + e.getMessage());
        }
        return new APIResult(false, "Player Unsuccessfully Edited.");
    }

    @CrossOrigin
    @RequestMapping(path = "/DeletePlayer", method = DELETE)
    public APIResult deletePlayer(
            @RequestParam(value = "id", required = false) Optional<Integer> id,
            @RequestParam(value = "username", required = false) String username) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        boolean result = false;
        try {
            if (id.isPresent()) {
                PersistencePlayer player = pPM.getPlayerById(id.get());
                result = pPM.deletePlayerById(id.get());
                if (result) {
                    return new APIResult(true, "Player " + player.getUsername() + " was deleted.");
                }
            } else {
                result = pPM.deletePlayerByUsername(username);
                if (result) {
                    return new APIResult(true, "Player " + username + " was deleted.");
                }
            }
        } catch (NoResultException e) {
            return new APIResult(false, "Player was not found");

        }
        return new APIResult(false, "Player was not found");
    }


    @RequestMapping("/GetPlayerWithHighestRating")
    public APIResult getPlayerWithHighestRating() {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        try {
            return new APIResult(true, pPM.writePlayerToJson(pPM.getPlayerWithHighestRating()));
        } catch (NoResultException e) {
            return new APIResult(false, e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping(path = "/GetPlayers", method = GET)
    public APIResult getPlayers() {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        List<Player> players = pPM.getViewPlayers();
        return new APIResult(true, pPM.writePlayerArrayToJson(players));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(path = "/GetAverageScore", method = GET)
    public APIResult getPlayerAverageScore(@RequestParam(value = "id") int id) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> gamesForPlayer = gPM.getGamesForPlayer(gPM.getPlayer(id));
        Player player = gPM.getPlayer(id);
        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(gamesForPlayer,
                player);

        return new APIResult(true, "[{ \"averageScore\" : \"" + calc.getAverageScore() + "\"}]");
    }

    @CrossOrigin
    @RequestMapping(path = "HardDeletePlayer", method = DELETE)
    public APIResult hardDeletePlayer(
            @RequestParam(value = "id", required = false) Optional<Integer> id,
            @RequestParam(value = "username", required = false) String username) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        boolean result = false;
        try {
            if (id.isPresent()) {
                PersistencePlayer player = pPM.getPlayerById(id.get());
                result = pPM.hardDeletePlayerById(id.get());
                if (result) {
                    return new APIResult(true, "Player " + player.getUsername() + " was deleted.");
                }
            } else {
                result = pPM.hardDeletePlayerByUsername(username);
                if (result) {
                    return new APIResult(true, "Player " + username + " was deleted.");
                }
            }
        } catch (NoResultException e) {
            return new APIResult(false, "Player was not found");
        }
        return new APIResult(false, "Player was not found");
    }
}