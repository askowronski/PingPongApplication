package app.API;


import app.API.GraphData.AverageScorePerGameData;
import app.API.GraphData.DateRangeData;
import app.API.GraphData.EloRatingGraphData;
import app.PersistenceManagers.EloRatingPersistenceManager;
import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.PersistenceModel.PersistencePlayerEloRatingList;
import app.ViewModel.EloRating;
import app.ViewModel.GameOutcomeEnum;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import app.StatsEngine.SinglePlayerStatisticsCalculator;
import app.StatsEngine.TotalGamesStatsCalculator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StatsAPI {

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/TotalWins")
    public APIResult getTotalWins(@RequestParam(value="id") int playerID,
            @RequestParam(value = "beginning", required = false) Optional<String> beginning,
            @RequestParam(value="end",required = false) Optional<String> end) {
        GamePersistenceManager gPM= new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayerByIDOld(playerID);
        if(player.getiD()==0){
            return new APIResult(false,"Player not found");
        }

        if (beginning.isPresent() && end.isPresent()) {
            Date beginningTime;
            Date endTime;
            try {

                beginningTime = new SimpleDateFormat("yyyyMMMdd").parse(beginning.get());
                endTime = new SimpleDateFormat("yyyyMMMdd").parse(end.get());
                List<PingPongGame> games = gPM.getGamesForPlayer(player, beginningTime, endTime);
                SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games,
                        player);
                return new APIResult(true, Integer.toString(calc.getWins()));
            } catch (ParseException p) {
                System.out.println(p.getMessage());
                return new APIResult(false, p.getMessage());

            }
        } else {
            List<PingPongGame> games = gPM.getGamesForPlayer(player);
            SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games,
                    player);
            return new APIResult(true, Integer.toString(calc.getWins()));
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/TotalGames")
    public APIResult getTotalGames(@RequestParam(value="id") int playerID) {
        GamePersistenceManager gPM= new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayerByIDOld(playerID);
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
        Player player = pPM.getPlayerByIDOld(playerID);
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
        Player player = pPM.getPlayerByIDOld(playerID);
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
        Player player = pPM.getPlayerByIDOld(playerID);
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
        Player player = pPM.getPlayerByIDOld(playerID);
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

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/GetPlayerOutcomes")
    public APIResult getPlayerOutcomes(@RequestParam(value="id") int playerID,
            @RequestParam(value="beginningTime") Optional<String> beginningTime ,
            @RequestParam(value="endingTime") Optional<String> endingTime
            ) {

        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        GamePersistenceManager gPM = new GamePersistenceManager();

        Player player = pPM.getPlayerByIDOld(playerID);
        Date begTime;
        Date endTime;
        List<PingPongGame> gamesForPlayer;


        try {
            gamesForPlayer = getGamesForPlayerOptionalDates(playerID, beginningTime, endingTime, gPM);
        } catch (ParseException pe) {
            return new APIResult(false, pe.getMessage());
        }

        if (gamesForPlayer.size() == 0) {
            return new APIResult(false, "No Games For Player.");
        }
        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(gamesForPlayer,player);

        List<GameOutcomeEnum> outcomes = calc.getOutcomes();

        List<Integer> netWins = new ArrayList<>();
        int runningCount = 0;

        for(GameOutcomeEnum outcome:outcomes){
            if(outcome.equals(GameOutcomeEnum.WIN)){
                runningCount+=1;
                netWins.add(runningCount);
            } else if(outcome.equals(GameOutcomeEnum.DRAW)){
                netWins.add(runningCount);
            } else {
                runningCount+=-1;
                netWins.add(runningCount);
            }
        }

        String json = "[";
        int i = 1;

        for(Integer count:netWins){
            json = json+"{\"wins\":"+count+",\"game\":"+gPM.writeGameToJson(gamesForPlayer.get(i-1))+",\"label\":"+i+"},";
            i++;
        }
        json = json.substring(0,json.length()-1);
        json= json+"]";


        return new APIResult(true,json);
    }

    private List<PingPongGame> getGamesForPlayerOptionalDates(@RequestParam(value = "id") int playerID,
            @RequestParam(value = "beginningTime") Optional<String> beginningTime,
            @RequestParam(value = "endingTime") Optional<String> endingTime,
            GamePersistenceManager gPM) throws ParseException {
        Date begTime;
        Date endTime;
        List<PingPongGame> gamesForPlayer;
        if (beginningTime.isPresent() && endingTime.isPresent()) {
            try {
                begTime = new SimpleDateFormat("yyyyMMMdd").parse(beginningTime.get());
                endTime = new SimpleDateFormat("yyyyMMMdd").parse(endingTime.get());
                return gPM.getGamesForPlayer(gPM.getPlayer(playerID),begTime,endTime);

            } catch (ParseException e) {
                throw e;
            }

        } else {
            return gPM.getGamesForPlayer(gPM.getPlayer(playerID));
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/GetPlayersGameDateRange")
    public APIResult getPlayersGameDateRange(@RequestParam(value="id") int playerID) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        List<PingPongGame> gamesForPlayer = gPM.getGamesForPlayer(gPM.getPlayer(playerID));
        Date begTime = gamesForPlayer.get(0).getTime();
        Date endTime = gamesForPlayer.get(gamesForPlayer.size()-1).getTime();

        ObjectMapper mapper = new ObjectMapper();

        String json="";

        try {
            json= mapper.writeValueAsString(new DateRangeData(begTime,endTime));
        } catch(JsonProcessingException e){
            json= e.getMessage();
        }

        return new APIResult(true, json);

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/GetAverageScores")
    public APIResult getAverageScores(@RequestParam(value="id") int playerID,
            @RequestParam(value="beginningTime") Optional<String> beginningTime,
            @RequestParam(value="endingTime") Optional<String> endingTime) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Date begTime;
        Date endTime;
        List<PingPongGame> gamesForPlayer;

        if (beginningTime.isPresent() && endingTime.isPresent()) {
            try {
                begTime = new SimpleDateFormat("yyyyMMMdd").parse(beginningTime.get());
                endTime = new SimpleDateFormat("yyyyMMMdd").parse(endingTime.get());
                gamesForPlayer = gPM.getGamesForPlayer(gPM.getPlayer(playerID),begTime,endTime);

            } catch (ParseException e) {
                return new APIResult(false,e.getMessage());
            }

        } else {
            gamesForPlayer = gPM.getGamesForPlayer(gPM.getPlayer(playerID));
        }

        if (gamesForPlayer.size() == 0) {
            return new APIResult(false, "No Games For Player.");
        }
        Player player = pPM.getViewPlayerByID(playerID,gamesForPlayer.get(gamesForPlayer.size()-1).getiD());

        Date theFirstDate = gamesForPlayer.get(0).getTime();
        Date theEndDate = gamesForPlayer.get(gamesForPlayer.size()-1).getTime();

        if(gamesForPlayer.size()==0){
            return new APIResult(false,"No Games For Player");
        }

        int i = 1;
        List<PingPongGame> runningGameList = new ArrayList<>();
        List<AverageScorePerGameData> dataList = new ArrayList<>();


        dataList.add(new AverageScorePerGameData(0,0,0,
                0,new PingPongGame(0,player,player,0,0),0,
                1500.00,gamesForPlayer.get(0).getOpponent(player).getRating().getRating(),theFirstDate,theEndDate));

        for(PingPongGame game:gamesForPlayer){
            int score = 0;
            int oppScore = 0;
            Player opponent = game.getOpponent(player);
            if(game.getPlayer1().getiD() == playerID){
                score = game.getPlayer1Score();
                oppScore = game.getPlayer2Score();
            } else {
                score = game.getPlayer2Score();
                oppScore = game.getPlayer1Score();
            }


            runningGameList.add(game);
            SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(runningGameList,player);
            AverageScorePerGameData data = new AverageScorePerGameData(calc.getAverageScore(),
                    score,calc.getOpponentAverageScore(),oppScore,game,i,player.getRating().getRating(),opponent.getRating().getRating(),theFirstDate,theEndDate);
            dataList.add(data);
            i++;
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json= mapper.writeValueAsString(dataList);
        } catch(JsonProcessingException e){
            json= e.getMessage();
        }

        return new APIResult(true,json);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/GetEloRatings")
    public APIResult getEloRatings(@RequestParam(value="id") int playerID,
            @RequestParam(value="beginningTime") Optional<String> beginningTime,
            @RequestParam(value="endingTime") Optional<String> endingTime) {

        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> gamesForPlayer;

        try {
            gamesForPlayer = this
                    .getGamesForPlayerOptionalDates(playerID, beginningTime, endingTime, gPM);
        } catch (ParseException pe) {
            return new APIResult(false, pe.getMessage());
        }

        if (gamesForPlayer.size() == 0) {
            return new APIResult(false, "No Games For Player.");
        }
        Player player = gPM.getPlayer(playerID);

        List<EloRatingGraphData> data = new ArrayList<>();

        data.add(new EloRatingGraphData(new PingPongGame(0,player,player,0,0),0,1500,1500));

        int i = 1;
        for(PingPongGame game:gamesForPlayer){
            Player opponenet = game.getOpponent(player);
            Player player1;
            if(game.getPlayer1().equals(opponenet)){
                player1 = game.getPlayer2();
            } else {
                player1 = game.getPlayer1();
            }



            data.add(new EloRatingGraphData(game,i,new EloRating(game.getPlayer1().getRating().getRating()).getRating(),
                    new EloRating(game.getPlayer2().getRating().getRating()).getRating()));
            i++;
        }
         String json;

        ObjectMapper mapper = new ObjectMapper();
        try {
            json= mapper.writeValueAsString(data);
        } catch(JsonProcessingException e){
            json= e.getMessage();
        }

        return new APIResult(true,json);
    }

}
