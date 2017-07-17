package app.API;


import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.StatsEngine.PingPongGame;
import app.StatsEngine.Player;
import app.StatsEngine.SinglePlayerStatisticsCalculator;
import app.StatsEngine.TotalGamesStatsCalculator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatsAPI {

    @CrossOrigin(origins = "http://localhost:3000")
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

    @CrossOrigin(origins = "http://localhost:3000")
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

    @CrossOrigin(origins = "http://localhost:3000")
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


    @CrossOrigin(origins = "http://localhost:3000")
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

    @CrossOrigin(origins = "http://localhost:3000")
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

    @CrossOrigin(origins = "http://localhost:3000")
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

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/TotalGameStats")
    public APIResult getTotalGameStats() {

        TotalGamesStatsCalculator tGSC = new TotalGamesStatsCalculator();


        return new APIResultStats();
    }
}
