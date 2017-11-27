package app.API;

import app.StatsEngine.TotalGamesStatsCalculator;


public class APIResultStats extends APIResult {

    private final int totalGames;
    private final double stdDevForLosses;
    private final double stdDevForGames;
    private final double averageEloRating;

    public APIResultStats(TotalGamesStatsCalculator tGSC) {
        super();
        this.totalGames = tGSC.getTotalGames();
        this.stdDevForLosses = tGSC.stdDeviationOfLosses();
        this.stdDevForGames = tGSC.stdDeviationOfGames();
        this.averageEloRating = tGSC.averageEloRating();


        this.setMessage("{\"totalGames\":"+totalGames+",\"stdDevForLosses\":"+stdDevForLosses+
                ",\"stdDevForGames\":"+stdDevForGames+",\"averageEloRating\":"+averageEloRating+"}");
        this.setSuccess(true);
    }
}
