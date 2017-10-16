package app.PersistenceManagers;


import app.Exceptions.InvalidParameterException;
import app.PersistenceModel.PersistenceGame;
import app.PersistenceModel.PersistencePlayer;
import app.PersistenceModel.PersistencePlayerEloRatingList;
import app.ViewModel.EloRating;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import app.ReadWriteFile.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javafx.util.Pair;
import javax.persistence.NoResultException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class GamePersistenceManager {

    private static String FILE_PATH = "pingPongGames.txt";

    public static String GET_GAMES = "from PersistenceGame as games where games.deleted = false";
    public static String GET_GAME_BY_ID = "from PersistenceGame as game where game.iD = :id";

    private final File file;

    private SessionFactory factory;

    public GamePersistenceManager() {
        this.file = new File(FILE_PATH);
        try {
            this.factory = HibernateConfiguration.SESSION_FACTORY;
        } catch (HibernateException e ) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public GamePersistenceManager(String filePath) {
        this.file = new File(filePath);
    }

    public void createGame(PersistenceGame game) throws InvalidParameterException {
        if (game.getTime().compareTo(new Date()) > 0) {
            throw new InvalidParameterException("Date cannot be greater than now.");
        }

        if(game.getPlayer2ID() == game.getPlayer1ID()) {
            throw new InvalidParameterException("Players must be different.");
        }

        if (game.getPlayer2Score() < 0 || game.getPlayer1Score() < 0) {
            throw new InvalidParameterException("Scores must be greater than or equal to 0.");
        }

        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        try {
            pPM.getPlayerById(game.getPlayer1ID());
            pPM.getPlayerById(game.getPlayer2ID());

            Session session = this.factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(game);
            transaction.commit();
            session.close();
        } catch (HibernateException | NoResultException e) {
            System.out.println(e.getMessage());
            throw e;
        }

        EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(game.getPlayer1ID());
        EloRatingPersistenceManager eRPM2 = new EloRatingPersistenceManager(game.getPlayer2ID());


       eRPM.updateEloRatingListOnCreateGame(game);

    }

    public Player getPlayer(int id){
        PlayerPersistenceManager ppm = new PlayerPersistenceManager();
        return ppm.getPlayerByIDOld(id);
    }

    public int getNextID() {
        List<PersistenceGame> games = this.getGamesNew();

        List<Integer> ids = new ArrayList<>();
        int highestID = 0;
        for(PersistenceGame game:games){
            if(game.getiD()>highestID){
                highestID=game.getiD();
            }
        }
        return highestID+1;
    }

    public PingPongGame getViewGameByID(int id) {
        List<PingPongGame> games = this.getGamesView();
        for(PingPongGame game:games){
            if(game.getiD()==id){
                return game;
            }
        }
        return new PingPongGame();
    }

    public PersistenceGame getGameByID(final int id) {
        try {
            Session session = factory.openSession();
            Query query = session.createQuery(GET_GAME_BY_ID);
            query.setParameter("id",id);
            Object obj = query.getSingleResult();
            return (PersistenceGame) obj;
        } catch (NoResultException | HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }


    public String writeGameToJson(PingPongGame game) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(game);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public String writeViewGamesToJson(List<PingPongGame> games) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(games);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public List<PingPongGame> getGamesView() {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        try {

            List<PersistenceGame> games = this.getGamesNew();
            List<PingPongGame> viewGames = new ArrayList<>();
            for(PersistenceGame game:games){
                Player player1 = pPM.getViewPlayerByID(game.getPlayer1ID(),game.getiD());
                Player player2 = pPM.getViewPlayerByID(game.getPlayer2ID(),game.getiD());
                viewGames.add(new PingPongGame(game.getiD(),player1,player2,game.getPlayer1Score(),game.getPlayer2Score(),game.getTime()));
            }
            return viewGames;
        } catch(HibernateException | NoResultException e){
            List<PingPongGame> games = new ArrayList<>();
            e.printStackTrace();
            return games;
        }
    }

    public List<PingPongGame> getGamesInBetweenDates(Date beginning,Date end) {
        List<PingPongGame> games = this.getGamesView();
        List<PingPongGame> gamesInBetween = new ArrayList<>();

        for (PingPongGame game:games) {
            if (game.getTime().compareTo(beginning) > 0 &&
                    game.getTime().compareTo(end) < 0) {
                gamesInBetween.add(game);
            }
        }

        return gamesInBetween;
    }

    public List<PersistenceGame> getGamesNew() {
        try{
            Session session = factory.openSession();
            Query query = session.createQuery(GET_GAMES);
            List<PersistenceGame> games =  query.getResultList();
            games.sort(Comparator.comparing(PersistenceGame::getTime));
            return games;
        } catch (NoResultException | HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }


    public void editWriteGameToFileNew(PersistenceGame newGame,
            PersistenceGame oldGame) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        if (newGame.getPlayer2ID() == newGame.getPlayer1ID()) {
            throw new IllegalArgumentException("Players must be different.");
        }

        if (newGame.getPlayer1Score() <0 || newGame.getPlayer2Score() < 0) {
            throw new IllegalArgumentException("Scores must be greater than or equal to 0.");
        }




        try {
            // validate players exist
            PersistencePlayer player1 = pPM.getPlayerById(newGame.getPlayer1ID());
            PersistencePlayer player2 = pPM.getPlayerById(newGame.getPlayer2ID());
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.update(newGame);
            transaction.commit();
            session.close();
        } catch (HibernateException | NoResultException e ) {
            System.out.println(e.getMessage());
            throw e;
        }
        EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(-1);
        eRPM.updateEloRatingsEditGame(oldGame, newGame);
    }



    public void deleteGamePropogate(PersistenceGame game){
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        EloRatingPersistenceManager eRPM1 = new EloRatingPersistenceManager(game.getPlayer1ID());


        eRPM1.updateEloRatingsOnDeleteGame(game,this.getGamesNew().indexOf(game));
        this.deleteGame(game);
    }

    public void hardDeleteGame(PersistenceGame game) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        EloRatingPersistenceManager eRPM1 = new EloRatingPersistenceManager(game.getPlayer1ID());

        GamePersistenceManager gPM = new GamePersistenceManager();


        eRPM1.updateEloRatingsOnDeleteGame(game,gPM.getGamesNew().indexOf(game));
        try {
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(game);
            transaction.commit();
            session.close();
        } catch (HibernateException e ) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void deleteGame(PersistenceGame game) {
        try {
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            game.setDeleted(true);
            session.update(game);
            transaction.commit();
            session.close();
        } catch (HibernateException e ) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public File getFile() {
        return this.file;
    }

    public List<PingPongGame> getGamesForPlayer(Player player) {
        List<PingPongGame> games = this.getGamesView();
        List<PingPongGame> gamesForPlayer = new ArrayList<>();

        for(PingPongGame game:games){
            if(game.getPlayer1().getiD() == player.getiD()) {
                gamesForPlayer.add(game);
            } else if (game.getPlayer2().getiD() == player.getiD()) {
                PingPongGame newGame = new PingPongGame(game.getiD(),game.getPlayer2(),
                        game.getPlayer1(),game.getPlayer2Score(),game.getPlayer1Score(),game.getTime());
                gamesForPlayer.add(newGame);
            }
        }
        return gamesForPlayer;
    }

    public List<PingPongGame> getGamesForPlayer(Player player, Date beginning, Date end) {
        List<PingPongGame> games = this.getGamesView();
        List<PingPongGame> gamesForPlayer = new ArrayList<>();

        for(PingPongGame game:games){
            Player player1 = game.getPlayer1();
            Player player2 = game.getPlayer2();
            EloRatingPersistenceManager eRPM1 = new EloRatingPersistenceManager(player1.getiD());
            EloRatingPersistenceManager eRPM2 = new EloRatingPersistenceManager(player2.getiD());

            EloRating rating1 = new EloRating(eRPM1.getRatingByGameID(game.getiD()).getEloRating());
            EloRating rating2 = new EloRating(eRPM2.getRatingByGameID(game.getiD()).getEloRating());

            player1.setRating(rating1);
            player2.setRating(rating2);



            if(game.getPlayer1().getiD() == player.getiD()) {
                if (game.getTime().compareTo(beginning) >= 0 && game.getTime().compareTo(end) <= 0) {
                    gamesForPlayer.add(game);
                }
            } else if (game.getPlayer2().getiD() == player.getiD()) {
                PingPongGame newGame = new PingPongGame(game.getiD(),game.getPlayer2(),
                        game.getPlayer1(),game.getPlayer2Score(),game.getPlayer1Score(),game.getTime());
                if (game.getTime().compareTo(beginning) >= 0 && game.getTime().compareTo(end) <= 0) {
                    gamesForPlayer.add(newGame);
                }
            }
        }
        return gamesForPlayer;
    }

    public List<PersistenceGame> getGamesForPlayer(int playerId) {
        List<PersistenceGame> games = this.getGamesNew();
        List<PersistenceGame> gamesForPlayer = new ArrayList<>();

        for(PersistenceGame game:games){
            if(game.getPlayer1ID() == playerId) {
                    gamesForPlayer.add(game);
            } else if (game.getPlayer2ID() == playerId) {
                PersistenceGame newGame = new PersistenceGame(game.getiD(),game.getPlayer2ID(),
                        game.getPlayer1ID(),game.getPlayer2Score(),game.getPlayer1Score(),game.getTime());
                    gamesForPlayer.add(newGame);
            }
        }
        return gamesForPlayer;
    }

    public Pair<Date,Date> getDateRangeOfGamesForPlayer(List<PingPongGame> games) {
        Date beginning = games.get(0).getTime();
        Date end = games.get(games.size()-1).getTime();
        return new Pair<>(beginning,end);
    }

    public List<PersistenceGame> reorderGames(List<PersistenceGame> games, PersistenceGame newGame) {
        games = this.removeGameById(games,newGame.getiD());
        for(int i = 0 ;i < games.size(); i++){
            if(games.get(i).getTime().compareTo(newGame.getTime()) > 0) {
                games.add(i,newGame);
                break;
            }
        }
        if (games.indexOf(newGame) < 0) {
            games.add(newGame);
        }

        return games;
    }

    private List<PersistenceGame> removeGameById(List<PersistenceGame> games, int id) {
        for(PersistenceGame game:games){
            if(game.getiD() == id){
                games.remove(game);
                break;
            }
        }
        return games;
    }
}
