package app.API;


import app.API.GraphData.AverageScorePerGameData;
import app.API.GraphData.DateRangeData;
import app.API.GraphData.EloRatingGraphData;
import app.PersistenceManagers.EloRatingPersistenceManager;
import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.PersistenceModel.PersistenceGame;
import app.PersistenceModel.PersistencePlayer;
import app.PersistenceModel.PersistencePlayerEloRatingList;
import app.StatsEngine.LongestStreakData;
import app.ViewModel.EloRating;
import app.ViewModel.GameOutcomeEnum;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import app.StatsEngine.SinglePlayerStatisticsCalculator;
import app.StatsEngine.TotalGamesStatsCalculator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
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
    public APIResult getTotalWins(@RequestParam(value = "id") int playerID,
            @RequestParam(value = "beginning", required = false) Optional<String> beginning,
            @RequestParam(value = "end", required = false) Optional<String> end) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getViewPlayerByID(playerID, 0);
        if (player.getiD() == 0) {
            return new APIResult(false, "Player not found");
        }

        if (beginning.isPresent() && end.isPresent()) {
            Date beginningTime;
            Date endTime;
            try {

                beginningTime = new SimpleDateFormat("yyyyMMMdd").parse(beginning.get());
                endTime = new SimpleDateFormat("yyyyMMMdd").parse(end.get());
                List<PersistenceGame> games = gPM
                        .getPersistenceGamesForPlayer(playerID, beginningTime, endTime);
                SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(
                        player);
                calc.setPersistenceGames(games);
                return new APIResult(true, Integer.toString(calc.getWins()));
            } catch (ParseException p) {
                System.out.println(p.getMessage());
                return new APIResult(false, p.getMessage());

            }
        } else {
            List<PersistenceGame> games = gPM.getPersistenceGamesForPlayer(player.getiD());
            SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(
                    player);
            calc.setPersistenceGames(games);
            return new APIResult(true, Integer.toString(calc.getWins()));
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/TotalGames")
    public APIResult getTotalGames(@RequestParam(value = "id") int playerID) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayerByIDOld(playerID);
        if (player.getiD() == 0) {
            return new APIResult(false, "Player not found");
        }

        List<PingPongGame> games = gPM.getGamesForPlayer(player);

        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games, player);
        return new APIResult(true, Integer.toString(calc.gamesPlayed()));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/StdDev")
    public APIResult getStdDev(@RequestParam(value = "id") int playerID) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayerByIDOld(playerID);
        if (player.getiD() == 0) {
            return new APIResult(false, "Player not found");
        }

        List<PingPongGame> games = gPM.getGamesForPlayer(player);

        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games, player);
        return new APIResult(true, Double.toString(calc.calculateStdDev()));
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/TotalLosses")
    public APIResult getTotalLosses(@RequestParam(value = "id") int playerID) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayerByIDOld(playerID);
        if (player.getiD() == 0) {
            return new APIResult(false, "Player not found");
        }

        List<PingPongGame> games = gPM.getGamesForPlayer(player);

        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games, player);
        return new APIResult(true, Integer.toString(calc.getLosses()));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/GetLongestStreak")
    public APIResult getLongestStreak() {
        TotalGamesStatsCalculator tGSC = new TotalGamesStatsCalculator();
        LongestStreakData lSD = tGSC.getLongestStreak();

        return new APIResult(true, lSD.writeDataToJson());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/HighestScore")
    public APIResult getHighestScore(@RequestParam(value = "id") int playerID) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayerByIDOld(playerID);
        if (player.getiD() == 0) {
            return new APIResult(false, "Player not found");
        }

        List<PingPongGame> games = gPM.getGamesForPlayer(player);

        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games, player);
        return new APIResult(true, Integer.toString(calc.getHighestScore()));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/AverageScore")
    public APIResult getAverageScore(@RequestParam(value = "id") int playerID) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Player player = pPM.getPlayerByIDOld(playerID);
        if (player.getiD() == 0) {
            return new APIResult(false, "Player not found");
        }

        List<PingPongGame> games = gPM.getGamesForPlayer(player);

        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(games, player);
        return new APIResult(true, Double.toString(calc.getAverageScore()));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/TotalGameStats")
    public APIResult getTotalGameStats() {

        TotalGamesStatsCalculator tGSC = new TotalGamesStatsCalculator();

        return new APIResultStats();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/GetPlayerOutcomes")
    public APIResult getPlayerOutcomes(@RequestParam(value = "id") int playerID,
            @RequestParam(value = "beginningTime") Optional<String> beginningTime,
            @RequestParam(value = "endingTime") Optional<String> endingTime
    ) {

        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        GamePersistenceManager gPM = new GamePersistenceManager();

        Player player = pPM.getViewPlayerByID(playerID,0);
        List<PersistenceGame> gamesForPlayer;

        try {
            gamesForPlayer = getPersistenceGamesForPlayerOptionalDates(playerID, beginningTime,
                    endingTime, gPM);
        } catch (ParseException pe) {
            return new APIResult(false, pe.getMessage());
        }

        if (gamesForPlayer.size() == 0) {
            return new APIResult(false, "No Games For Player.");
        }
        SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(player);
        calc.setPersistenceGames(gamesForPlayer);

        List<GameOutcomeEnum> outcomes = calc.getOutcomes();

        List<Integer> netWins = new ArrayList<>();
        int runningCount = 0;

        for (GameOutcomeEnum outcome : outcomes) {
            if (outcome.equals(GameOutcomeEnum.WIN)) {
                runningCount += 1;
                netWins.add(runningCount);
            } else if (outcome.equals(GameOutcomeEnum.DRAW)) {
                netWins.add(runningCount);
            } else {
                runningCount += -1;
                netWins.add(runningCount);
            }
        }

        String json = "[";
        int i = 1;

        DateFormat sDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Integer count : netWins) {
            PersistenceGame theGame = gamesForPlayer.get(i - 1);
            json = json + "{\"wins\":" + count + ",\"game\":" + gPM.writeGameToJson(theGame) + ","
                    + "\"player1Username\":\"" + pPM.getPlayerById(theGame.getPlayer1ID())
                    .getUsername() + "\","
                    + "\"player2Username\":\"" + pPM.getPlayerById(theGame.getPlayer2ID())
                    .getUsername() + "\","
                    + "\"timeString\":\"" + sDF.format(theGame.getTime()) + "\","
                    + "\"label\":" + i + "},";
            i++;
        }
        json = json.substring(0, json.length() - 1);
        json = json + "]";

        return new APIResult(true, json);
    }

    private List<PingPongGame> getGamesForPlayerOptionalDates(
            @RequestParam(value = "id") int playerID,
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
                return gPM.getGamesForPlayer(gPM.getPlayer(playerID), begTime, endTime);

            } catch (ParseException e) {
                throw e;
            }

        } else {
            return gPM.getGamesForPlayer(gPM.getPlayer(playerID));
        }
    }

    private List<PersistenceGame> getPersistenceGamesForPlayerOptionalDates(
            @RequestParam(value = "id") int playerID,
            @RequestParam(value = "beginningTime") Optional<String> beginningTime,
            @RequestParam(value = "endingTime") Optional<String> endingTime,
            GamePersistenceManager gPM) throws ParseException {
        Date begTime;
        Date endTime;
        if (beginningTime.isPresent() && endingTime.isPresent()) {
            try {
                begTime = new SimpleDateFormat("yyyyMMMdd").parse(beginningTime.get());
                endTime = new SimpleDateFormat("yyyyMMMdd").parse(endingTime.get());
                return gPM.getPersistenceGamesForPlayer(playerID, begTime, endTime);

            } catch (ParseException e) {
                throw e;
            }

        } else {
            return gPM.getPersistenceGamesForPlayer(gPM.getPlayer(playerID).getiD());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/GetPlayersGameDateRange")
    public APIResult getPlayersGameDateRange(@RequestParam(value = "id") int playerID) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        List<PingPongGame> gamesForPlayer = gPM.getGamesForPlayer(gPM.getPlayer(playerID));
        Date begTime = gamesForPlayer.get(0).getTime();
        Date endTime = gamesForPlayer.get(gamesForPlayer.size() - 1).getTime();

        ObjectMapper mapper = new ObjectMapper();

        String json = "";

        try {
            json = mapper.writeValueAsString(new DateRangeData(begTime, endTime));
        } catch (JsonProcessingException e) {
            json = e.getMessage();
        }

        return new APIResult(true, json);

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/GetAverageScores")
    public APIResult getAverageScores(@RequestParam(value = "id") int playerID,
            @RequestParam(value = "beginningTime") Optional<String> beginningTime,
            @RequestParam(value = "endingTime") Optional<String> endingTime) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        Date begTime;
        Date endTime;
        List<PersistenceGame> gamesForPlayer;

        if (beginningTime.isPresent() && endingTime.isPresent()) {
            try {
                begTime = new SimpleDateFormat("yyyyMMMdd").parse(beginningTime.get());
                endTime = new SimpleDateFormat("yyyyMMMdd").parse(endingTime.get());
                gamesForPlayer = gPM.getPersistenceGamesForPlayer(playerID, begTime, endTime);

            } catch (ParseException e) {
                return new APIResult(false, e.getMessage());
            }

        } else {
            gamesForPlayer = gPM.getPersistenceGamesForPlayer(gPM.getPlayer(playerID).getiD());
        }

        if (gamesForPlayer.size() == 0) {
            return new APIResult(false, "No Games For Player.");
        }
        Player player = pPM
                .getViewPlayerByID(playerID, gamesForPlayer.get(gamesForPlayer.size() - 1).getiD());

        Date theFirstDate = gamesForPlayer.get(0).getTime();
        Date theEndDate = gamesForPlayer.get(gamesForPlayer.size() - 1).getTime();

        if (gamesForPlayer.size() == 0) {
            return new APIResult(false,
                    "No games for " + player.getUsername() + " in this date range.");
        }

        int i = 1;
        List<PersistenceGame> runningGameList = new ArrayList<>();
        List<AverageScorePerGameData> dataList = new ArrayList<>();

        dataList.add(new AverageScorePerGameData(0, 0, 0,
                0, new PingPongGame(0, player, player, 0, 0), 0, theFirstDate, theEndDate));

        for (PersistenceGame game : gamesForPlayer) {
            int score = 0;
            int oppScore = 0;
            int opponentId;

            if (game.getPlayer1ID() == playerID) {
                score = game.getPlayer1Score();
                oppScore = game.getPlayer2Score();
                opponentId = game.getPlayer2ID();
            } else {
                score = game.getPlayer2Score();
                oppScore = game.getPlayer1Score();
                opponentId = game.getPlayer1ID();
            }

            runningGameList.add(game);
            PingPongGame viewGame = new PingPongGame(game.getiD(),
                    pPM.getViewPlayerByID(playerID, game.getiD()),
                    pPM.getViewPlayerByID(opponentId, game.getiD()), score, oppScore,
                    game.getTime());
            SinglePlayerStatisticsCalculator calc = new SinglePlayerStatisticsCalculator(player);
            calc.setPersistenceGames(runningGameList);
            AverageScorePerGameData data = new AverageScorePerGameData(calc.getAverageScore(),
                    score, calc.getOpponentAverageScore(), oppScore, viewGame, i, theFirstDate,
                    theEndDate);
            dataList.add(data);
            i++;
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(dataList);
        } catch (JsonProcessingException e) {
            json = e.getMessage();
        }

        return new APIResult(true, json);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/GetEloRatings")
    public APIResult getEloRatings(@RequestParam(value = "id") int playerID,
            @RequestParam(value = "beginningTime") Optional<String> beginningTime,
            @RequestParam(value = "endingTime") Optional<String> endingTime) {

        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        List<PersistenceGame> gamesForPlayer;

        try {
            gamesForPlayer = this
                    .getPersistenceGamesForPlayerOptionalDates(playerID, beginningTime, endingTime,
                            gPM);
        } catch (ParseException pe) {
            return new APIResult(false, pe.getMessage());
        }

        if (gamesForPlayer.size() == 0) {
            return new APIResult(false, "No Games For Player.");
        }
        PersistencePlayer player = pPM.getPlayerById(playerID);

        List<EloRatingGraphData> data = new ArrayList<>();

        data.add(new EloRatingGraphData(
                new PingPongGame(0, new Player(new EloRating(1500), 0, "", "", ""),
                        new Player(new EloRating(1500), 0, "", "", ""), 0, 0), 0, 1500, 1500));

        int i = 1;
        for (PersistenceGame game : gamesForPlayer) {
            Player player1;
            Player player2;
            if (game.getPlayer1ID() == player.getId()) {
                player1 = pPM.getViewPlayerByID(player.getId(), game.getiD());
                player2 = pPM.getViewPlayerByID(game.getPlayer2ID(), game.getiD());
            } else {
                player1 = pPM.getViewPlayerByID(player.getId(), game.getiD());
                player2 = pPM.getViewPlayerByID(game.getPlayer1ID(), game.getiD());
            }

            PingPongGame newGame = new PingPongGame(game.getiD(), player1, player2,
                    game.getPlayer1Score(), game.getPlayer2Score(), game.getTime());

            data.add(new EloRatingGraphData(newGame, i, player1.getRating().getRating(),
                    player2.getRating().getRating()));
            i++;
        }
        String json;

        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            json = e.getMessage();
        }

        return new APIResult(true, json);
    }

}
