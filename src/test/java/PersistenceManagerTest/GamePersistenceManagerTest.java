package PersistenceManagerTest;


import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceModel.PersistenceEloRating;
import app.PersistenceModel.PersistenceGame;
import app.PersistenceModel.PersistencePlayerEloRatingList;
import app.ViewModel.EloRating;
import app.ViewModel.GameOutcomeEnum;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class GamePersistenceManagerTest {

//    public static List<PersistenceGame> games = new ArrayList<>();
//    PersistencePlayerEloRatingList list1 = new PersistencePlayerEloRatingList();
//    PersistencePlayerEloRatingList list2 = new PersistencePlayerEloRatingList();
//
//    @BeforeAll
//    public static void beforeAll() {
//
//        PersistenceGame game1 = new PersistenceGame(1, 2,
//                2, 15, 14, new Date(2017, 9, 13));
//        PersistenceGame game2 = new PersistenceGame(2, 2, 2,
//                15, 14, new Date(2017, 10, 13));
//        PersistenceGame game3 = new PersistenceGame(3, 2, 2,
//                15, 14, new Date(2017, 11, 13));
//
//        games.add(game1);
//        games.add(game2);
//        games.add(game3);
//    }
//
//    @Test
//    public void testReorderGames() {
//
//        PersistenceGame game4 = new PersistenceGame(4, 2, 2,
//                15, 14, new Date(2017, 10, 15));
//
//        GamePersistenceManager gPM = new GamePersistenceManager();
//        games = gPM.reorderGames(games, game4);
//
//        assertEquals(games.get(2), game4);
//    }
//
//    @Test
//    public void testEditGameUpdatePlayersEloRatings() {
//
//    }
//
//    public void createGame(PersistencePlayerEloRatingList list1,
//            PersistencePlayerEloRatingList list2, PersistenceGame game) {
//        PersistenceEloRating rating1 = new PersistenceEloRating();
//        PersistenceEloRating rating2 = new PersistenceEloRating();
//        GameOutcomeEnum outcome1;
//        GameOutcomeEnum outcome2;
//        if (game.getPlayer2Score() > game.getPlayer1Score()) {
//            outcome1 = GameOutcomeEnum.LOSS;
//            outcome2 = GameOutcomeEnum.WIN;
//        } else {
//            outcome2 = GameOutcomeEnum.LOSS;
//            outcome1 = GameOutcomeEnum.WIN;
//        }
//
//        PersistenceEloRating newRating1 = new PersistenceEloRating();
//        PersistenceEloRating newRating2 = new PersistenceEloRating();
//
//        newRating1 = rating1.newRating(outcome1, rating2, game.getiD());
//        newRating2 = rating2.newRating(outcome2, rating1, game.getiD());
//
//        list1.addEloRating(newRating1);
//        list2.addEloRating(newRating2);
//
//    }

}
