package app.API;

import app.StatsEngine.TotalGamesStatsCalculator;

/**
 * Created by askowronski on 7/17/17.
 */
public class APIResultStats extends APIResult {

    private final int totalGames;
    private final double stdDevForLosses;
    private final double stdDevForGames;
    private final double averageEloRating;

    public APIResultStats() {
        super(true,"{\"totalGames\":"+new TotalGamesStatsCalculator().getTotalGames()+",\"stdDevForLosses\":"+new TotalGamesStatsCalculator().stdDeviationOfLosses()+
                ",\"stdDevForGames\":"+new TotalGamesStatsCalculator().stdDeviationOfGames()+",\"averageEloRating\":"+new TotalGamesStatsCalculator().averageEloRating()+"}");
        TotalGamesStatsCalculator tGSC = new TotalGamesStatsCalculator();
        this.totalGames = tGSC.getTotalGames();
        this.stdDevForLosses = tGSC.stdDeviationOfLosses();
        this.stdDevForGames = tGSC.stdDeviationOfGames();
        this.averageEloRating = tGSC.averageEloRating();
    }
}
