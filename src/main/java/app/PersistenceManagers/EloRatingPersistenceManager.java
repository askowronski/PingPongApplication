package app.PersistenceManagers;


import app.PersistenceModel.PersistenceEloRating;
import app.PersistenceModel.PersistenceGame;
import app.PersistenceModel.PersistencePlayer;
import app.PersistenceModel.PersistencePlayerEloRatingList;
import app.ReadWriteFile.File;
import app.ViewModel.GameOutcomeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class EloRatingPersistenceManager {

    private static String FILE_PATH = "pingPongEloRatingID";
    private final int playerID;

    private static String FIND_ELO_RATINGS_FOR_PLAYER = "from PersistenceEloRating as ratings where ratings.playerID = :playerid";
    private static String FIND_ELO_RATING_BY_GAME_ID = "from PersistenceEloRating as ratings where ratings.playerID = :playerid and "
            + "ratings.gameID=:gameId";

    private SessionFactory factory;

    public EloRatingPersistenceManager(String filePath, int playerID) {
        this.playerID = playerID;
        try {
            this.factory = HibernateConfiguration.SESSION_FACTORY;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public EloRatingPersistenceManager(int playerID) {
        String path = FILE_PATH + playerID + ".txt";
        this.playerID = playerID;
        try {
            this.factory = HibernateConfiguration.SESSION_FACTORY;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void createEloRating(PersistenceEloRating eloRating) {
        try {
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(eloRating);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void deleteEloRating(int gameId) {
        try {
            PersistenceEloRating rating = this.getRatingByGameID(gameId);
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(rating);
            transaction.commit();
            session.close();
        } catch (NoResultException | HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }

    }


    public int getPlayerID() {
        return playerID;
    }

    public void saveOrUpdateEloList(PersistencePlayerEloRatingList ratings) {
        try {
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            ratings.setSortOrder();
            for (PersistenceEloRating rating : ratings.getList()) {
                session.saveOrUpdate(rating);
            }
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            factory.getCurrentSession().close();
            throw e;
        }
    }

    public void saveEloRating(int gameId) {
        try {
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(this.getEloRatingList()
                    .getRating(this.getEloRatingList().getIndexOfGame(gameId)));
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            factory.getCurrentSession().close();
            throw e;
        }
    }

    public PersistencePlayerEloRatingList getEloRatingList() {
        try {
            Session session = factory.openSession();
            Query query = session.createQuery(FIND_ELO_RATINGS_FOR_PLAYER);
            query.setParameter("playerid", this.getPlayerID());
            List<PersistenceEloRating> ratings = query.list();
            Collections.sort(ratings,
                    Comparator.comparing((PersistenceEloRating rating) -> rating.getSortOrder()));
            return new PersistencePlayerEloRatingList(new LinkedList<>(ratings));
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }



    public PersistenceEloRating getRatingByGameID(int gameID) {
        try {
            Session session = factory.openSession();
            Query query = session.createQuery(FIND_ELO_RATING_BY_GAME_ID);
            query.setParameter("playerid", this.getPlayerID());
            query.setParameter("gameId",gameID);
            PersistenceEloRating rating = (PersistenceEloRating)query.getSingleResult();
            return rating;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public PersistenceEloRating getRatingPriorToGame(int gameId) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PersistenceGame> list = gPM.getGamesForPlayer(this.getPlayerID());
        List<PersistenceEloRating> elos = this.getEloRatingList().getList();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getiD() == gameId) {
                return elos.get(i);
            }
        }
        throw new EntityNotFoundException("No rating found prior to gameId " + gameId + ".");
    }

    public PersistenceEloRating getRatingByGameID(int gameID, PersistencePlayerEloRatingList list) {
        return list.getRating(list.getIndexOfGame(gameID));
    }

    public String writeEloRatingListToJson(PersistencePlayerEloRatingList ratings) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(ratings);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public void updateEloRatingListOnCreateGame(
            PersistenceGame game) {
        GameOutcomeEnum outcome1;
        GameOutcomeEnum outcome2;
        if (game.getPlayer1Score() > game.getPlayer2Score()) {
            outcome1 = GameOutcomeEnum.WIN;
            outcome2 = GameOutcomeEnum.LOSS;
        } else if (game.getPlayer2Score() > game.getPlayer1Score()) {
            outcome1 = GameOutcomeEnum.LOSS;
            outcome2 = GameOutcomeEnum.WIN;

        } else {
            outcome1 = GameOutcomeEnum.DRAW;
            outcome2 = GameOutcomeEnum.DRAW;
        }

        GamePersistenceManager gPM = new GamePersistenceManager();

        EloRatingPersistenceManager eRPM1 = new EloRatingPersistenceManager(game.getPlayer1ID());
        EloRatingPersistenceManager eRPM2 = new EloRatingPersistenceManager(game.getPlayer2ID());

        List<PersistenceGame> games = gPM.getGamesNew();

        int indexOfGame = games.indexOf(game);

        PersistenceEloRating ratingPriorToGame1 = eRPM1.getRatingPriorToGame(game.getiD());
        PersistenceEloRating ratingPriorToGame2 = eRPM2.getRatingPriorToGame(game.getiD());

        PersistenceEloRating newRating1 = ratingPriorToGame1
                .newRating(outcome1, ratingPriorToGame2, game.getiD());
        PersistenceEloRating newRating2 = ratingPriorToGame2
                .newRating(outcome2, ratingPriorToGame1, game.getiD());

        int gameIdPriorToCreatedGame;

        if (indexOfGame > 0) {
            gameIdPriorToCreatedGame = games.get(indexOfGame - 1).getiD();
        } else {
            gameIdPriorToCreatedGame = 0;
        }

        PersistencePlayerEloRatingList ratings1 = eRPM1.getEloRatingList();
        PersistencePlayerEloRatingList ratings2 = eRPM2.getEloRatingList();

        int indexOfElo1 = ratings1.getIndexOfGame(gameIdPriorToCreatedGame);
        int indexOfElo2 = ratings2.getIndexOfGame(gameIdPriorToCreatedGame);

        ratings1.addEloRating(indexOfElo1 + 1, newRating1);
        ratings2.addEloRating(indexOfElo2 + 1, newRating2);

        eRPM1.saveOrUpdateEloList(ratings1);
        eRPM2.saveOrUpdateEloList(ratings2);

        this.updateAllEloRatingsFromGameIndexOn(indexOfGame + 1, gPM);
    }

    public void updateEloRatingsOnDeleteGame(PersistenceGame game, int indexOfGame) {
        GamePersistenceManager gPM = new GamePersistenceManager();

//        List<PersistenceGame> games = gPM.getGamesNew();

        EloRatingPersistenceManager eRPM1 = new EloRatingPersistenceManager(game.getPlayer1ID());
        EloRatingPersistenceManager eRPM2 = new EloRatingPersistenceManager(game.getPlayer2ID());

        eRPM1.deleteEloRating(game.getiD());
        eRPM2.deleteEloRating(game.getiD());

        eRPM1.saveOrUpdateEloList(eRPM1.getEloRatingList());
        eRPM2.saveOrUpdateEloList(eRPM2.getEloRatingList());

        this.updateAllEloRatingsFromGameIndexOn(indexOfGame + 1, gPM);
    }

    public void updateEloRatingsEditGame(PersistenceGame oldGame, PersistenceGame newGame) {
        GamePersistenceManager gPM = new GamePersistenceManager();

        List<PersistenceGame> games = gPM.getGamesNew();

        EloRatingPersistenceManager eRPM1 = new EloRatingPersistenceManager(newGame.getPlayer1ID());
        EloRatingPersistenceManager eRPM2 = new EloRatingPersistenceManager(newGame.getPlayer2ID());

        int indexOfGame = games.indexOf(newGame);

        if (!this.arePlayersAndOutcomesTheSame(oldGame, newGame)) {
            if (this.areOutcomesSame(oldGame, newGame)) {
                this.updateAllEloRatingsFromGameIndexOn(indexOfGame, gPM);
            } else {
                if (!newGame.isPlayerInGame(oldGame.getPlayer1ID())) {
                    EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(
                            oldGame.getPlayer1ID());
                    eRPM.deleteEloRating(oldGame.getiD());
                }

                if (!newGame.isPlayerInGame(oldGame.getPlayer2ID())) {
                    EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(
                            oldGame.getPlayer1ID());
                    eRPM.deleteEloRating(oldGame.getiD());
                }
            }
        }

        this.updateAllEloRatingsFromGameIndexOn(indexOfGame, gPM);
    }

    public boolean arePlayersSame(PersistenceGame game1, PersistenceGame game2) {
        boolean check = true;

        if (game1.getPlayer1ID() == game2.getPlayer2ID() ||
                game1.getPlayer1ID() == game2.getPlayer1ID()) {
            if (game1.getPlayer2ID() == game2.getPlayer1ID() ||
                    game1.getPlayer2ID() == game2.getPlayer2ID()) {
                if (game1.getPlayer1ID() == game2.getPlayer1ID()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean areOutcomesSame(PersistenceGame game1, PersistenceGame game2) {
        if (this.arePlayersSame(game1, game2)) {
            if (game1.getPlayer1ID() == game2.getPlayer1ID()) {
                return game1.getOucome(game1.getPlayer1ID())
                        .equals(game2.getOucome(game2.getPlayer1ID()));
            } else {
                return game1.getOucome(game1.getPlayer1ID())
                        .equals(game2.getOucome(game2.getPlayer2ID()));
            }
        }
        return false;
    }

    public boolean arePlayersAndOutcomesTheSame(PersistenceGame game1, PersistenceGame game2) {
        boolean check = true;
        return this.areOutcomesSame(game1, game2);
    }

    public void updateAllEloRatingsFromGameIndexOn(int index,
            GamePersistenceManager gPM) {
        List<PersistenceGame> games = gPM.getGamesNew();

        if (index >= games.size()) {
            return;
        }

        for (int i = index; i < games.size(); i++) {
            PersistenceGame game = games.get(i);
            EloRatingPersistenceManager eRPM1 = new EloRatingPersistenceManager(
                    game.getPlayer1ID());
            EloRatingPersistenceManager eRPM2 = new EloRatingPersistenceManager(
                    game.getPlayer2ID());
            PersistenceEloRating newRating1;
            PersistenceEloRating newRating2;

            int gameIdPriorToGame1;
            int gameIdPriorToGame2;


            if (i - 1 < 0) {
                gameIdPriorToGame1 = 0;
                gameIdPriorToGame2 = 0;

            } else {
                List<PersistenceGame> games1 = gPM.getGamesForPlayer(game.getPlayer1ID());
                List<PersistenceGame> games2 = gPM.getGamesForPlayer(game.getPlayer2ID());
                if (games1.indexOf(game) == 0) {
                    gameIdPriorToGame1 = 0;
                } else {
                    gameIdPriorToGame1= games.get(games1.indexOf(game)-1).getiD();
                }

                if (games2.indexOf(game) == 0) {
                    gameIdPriorToGame2 = 0;
                } else {
                    gameIdPriorToGame2= games.get(games1.indexOf(game)-1).getiD();
                }

            }

            PersistenceEloRating ratingPriorToGame1 = eRPM1.getRatingByGameID(gameIdPriorToGame1);
            PersistenceEloRating ratingPriorToGame2 = eRPM2.getRatingByGameID(gameIdPriorToGame2);

            if (game.didWin(game.getPlayer1ID())) {
                newRating1 = ratingPriorToGame1
                        .newRating(GameOutcomeEnum.WIN, ratingPriorToGame2, game.getiD());
                newRating2 = ratingPriorToGame2
                        .newRating(GameOutcomeEnum.LOSS, ratingPriorToGame1, game.getiD());
            } else if (game.didLose(game.getPlayer1ID())) {
                newRating1 = ratingPriorToGame1
                        .newRating(GameOutcomeEnum.LOSS, ratingPriorToGame2, game.getiD());
                newRating2 = ratingPriorToGame2
                        .newRating(GameOutcomeEnum.WIN, ratingPriorToGame1, game.getiD());
            } else {
                newRating1 = ratingPriorToGame1
                        .newRating(GameOutcomeEnum.DRAW, ratingPriorToGame2, game.getiD());
                newRating2 = ratingPriorToGame2
                        .newRating(GameOutcomeEnum.DRAW, ratingPriorToGame1, game.getiD());
            }

            PersistencePlayerEloRatingList list1 = eRPM1.getEloRatingList();
            PersistencePlayerEloRatingList list2 = eRPM2.getEloRatingList();

            list1.replaceEloRatingWithGameId(game.getiD(), newRating1);
            list2.replaceEloRatingWithGameId(game.getiD(), newRating2);

            eRPM1.saveOrUpdateEloList(list1);
            eRPM2.saveOrUpdateEloList(list2);
        }
    }

    public void hardDeleteEloRatings() {
        PersistencePlayerEloRatingList list = this.getEloRatingList();
        try {
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            for (PersistenceEloRating rating : list.getList()) {
                session.delete(rating);
            }
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            factory.getCurrentSession().close();
            throw e;
        }

    }

    public String writeEloRatingListToJson(List<PersistenceEloRating> ratings) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(ratings);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

}
