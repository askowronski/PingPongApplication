package app.API;


import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.StatsEngine.PingPongGame;
import app.StatsEngine.Player;
import app.StatsEngine.SinglePlayerStatisticsCalculator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatsAPI {

    @RequestMapping("/TotalWins")
    public APIResult getTotalWins(@RequestParam(value="id") int playerID) {
        GamePersistenceManager gPM= new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayer(playerID);
        if(player.getiD()==0){
            return new APIResult(false,"Player not found");
        }

        List<PingPongGame> games = gPM.getGamesForPlayer(player);

        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games,player);
        return new APIResult(true,Integer.toString(calc.getWins()));
    }

    @RequestMapping("/TotalGames")
    public APIResult getTotalGames(@RequestParam(value="id") int playerID) {
        GamePersistenceManager gPM= new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayer(playerID);
        if(player.getiD()==0){
            return new APIResult(false,"Player not found");
        }

        List<PingPongGame> games = gPM.getGamesForPlayer(player);

        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games,player);
        return new APIResult(true,Integer.toString(calc.gamesPlayed()));
    }

    @RequestMapping("/StdDev")
    public APIResult getStdDev(@RequestParam(value="id") int playerID) {
        GamePersistenceManager gPM= new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayer(playerID);
        if(player.getiD()==0){
            return new APIResult(false,"Player not found");
        }

        List<PingPongGame> games = gPM.getGamesForPlayer(player);

        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games,player);
        return new APIResult(true,Double.toString(calc.calculateStdDev()));
    }

    @RequestMapping("/TotalLosses")
    public APIResult getTotalLosses(@RequestParam(value="id") int playerID) {
        GamePersistenceManager gPM= new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayer(playerID);
        if(player.getiD()==0){
            return new APIResult(false,"Player not found");
        }

        List<PingPongGame> games = gPM.getGamesForPlayer(player);

        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games,player);
        return new APIResult(true,Integer.toString(calc.getLosses()));
    }

    @RequestMapping("/HighestScore")
    public APIResult getHighestScore(@RequestParam(value="id") int playerID) {
        GamePersistenceManager gPM= new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayer(playerID);
        if(player.getiD()==0){
            return new APIResult(false,"Player not found");
        }

        List<PingPongGame> games = gPM.getGamesForPlayer(player);

        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games,player);
        return new APIResult(true,Integer.toString(calc.getHighestScore()));
    }

    @RequestMapping("/AverageScore")
    public APIResult getAverageScore(@RequestParam(value="id") int playerID) {
        GamePersistenceManager gPM= new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayer(playerID);
        if(player.getiD()==0){
            return new APIResult(false,"Player not found");
        }

        List<PingPongGame> games = gPM.getGamesForPlayer(player);

        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games,player);
        return new APIResult(true,Double.toString(calc.getAverageScore()));
    }
}
