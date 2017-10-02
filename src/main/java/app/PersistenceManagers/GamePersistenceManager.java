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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
            Configuration config = new Configuration().configure();
            config.addAnnotatedClass(PersistenceGame.class);
            this.factory = config.buildSessionFactory();
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

        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        try {
            Session session = this.factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(game);
            transaction.commit();
            session.flush();
            session.close();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }

        pPM.updatePlayersEloOnCreateGame(game,this);

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
            return query.getResultList();
        } catch (NoResultException | HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }


    public void editWriteGameToFileNew(PersistenceGame newGame,
            PersistenceGame oldGame) {
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();


        try {
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.update(newGame);
            transaction.commit();
            session.flush();
            session.close();
        } catch (HibernateException e ) {
            System.out.println(e.getMessage());
            throw e;
        }

        pPM.updatePlayersEloRatingEdit(newGame,oldGame,this);
    }



    public void deleteGamePropogate(PersistenceGame game){
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        EloRatingPersistenceManager eRPM1 = new EloRatingPersistenceManager(game.getPlayer1ID());
        eRPM1.deleteEloRating(game.getiD());
        EloRatingPersistenceManager eRPM2 = new EloRatingPersistenceManager(game.getPlayer2ID());
        eRPM2.deleteEloRating(game.getiD());

        List<PersistenceGame> games = this.getGamesNew();
        int indexOfGame = games.indexOf(game);

        try {
            this.deleteGame(game);
            if (indexOfGame < games.size()) {
                pPM.updatePlayersEloRatingEdit(games.get(indexOfGame), games.get(indexOfGame), this);
            }
        } catch (HibernateException e) {
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
            session.flush();
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
            if(game.getPlayer1().getiD() == player.getiD()) {
                if (game.getTime().compareTo(beginning) > 0 && game.getTime().compareTo(end) < 0) {
                    gamesForPlayer.add(game);
                }
            } else if (game.getPlayer2().getiD() == player.getiD()) {
                PingPongGame newGame = new PingPongGame(game.getiD(),game.getPlayer2(),
                        game.getPlayer1(),game.getPlayer2Score(),game.getPlayer1Score(),game.getTime());
                if (game.getTime().compareTo(beginning) > 0 && game.getTime().compareTo(end) < 0) {
                    gamesForPlayer.add(newGame);
                }
            }
        }
        return gamesForPlayer;
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
