package app.StatsEngine;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;


public class EloRatingTest {

    @Test
    public void testExpectedScoreSimple() {
        EloRating rating1 = new EloRating();
        EloRating rating2 = new EloRating();

        double expectedScore = rating1.expectedScore(rating2);

        assertTrue(expectedScore == .5);
    }

    @Test
    public void testExpectedScoreWideDifference() {
        EloRating rating1 = new EloRating();
        EloRating rating2 = new EloRating(100);

        double expectedScore = rating1.expectedScore(rating2);

        assertTrue(expectedScore == (double)15/16);
    }

    @Test
    public void testNewRatingSameScore() {
        EloRating rating1 = new EloRating();
        EloRating rating2 = new EloRating();

        EloRating newRating = rating1.newRating(GameOutcome.WIN,rating2);
        assertTrue(newRating.getRating() == (double)(rating1.getRating()+(EloRating.K_FACTOR/2)));
    }

    @Test
    public void testNewRatingWin() {
        EloRating rating1 = new EloRating();
        EloRating rating2 = new EloRating(400);

        EloRating newRating = rating1.newRating(GameOutcome.WIN,rating2);
        assertTrue(newRating.getRating() == (double)(rating1.getRating()+(EloRating.K_FACTOR*(1-rating1.expectedScore(rating2)))));
    }

    @Test
    public void testNewRatingDraw() {
        EloRating rating1 = new EloRating();
        EloRating rating2 = new EloRating(400);

        EloRating newRating = rating1.newRating(GameOutcome.DRAW,rating2);
        assertTrue(newRating.getRating() == (double)(rating1.getRating()+(EloRating.K_FACTOR*(0-rating1.expectedScore(rating2)))));
    }

    @Test
    public void testNewRatingLoss() {
        EloRating rating1 = new EloRating();
        EloRating rating2 = new EloRating(400);

        EloRating newRating = rating1.newRating(GameOutcome.LOSS,rating2);
        assertTrue(newRating.getRating() == (double)(rating1.getRating()+(EloRating.K_FACTOR*(-1-rating1.expectedScore(rating2)))));
    }


}
