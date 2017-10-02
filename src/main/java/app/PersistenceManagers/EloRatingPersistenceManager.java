package app.PersistenceManagers;


import app.PersistenceModel.PersistenceEloRating;
import app.PersistenceModel.PersistenceGame;
import app.PersistenceModel.PersistencePlayer;
import app.PersistenceModel.PersistencePlayerEloRatingList;
import app.ReadWriteFile.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.NoResultException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class EloRatingPersistenceManager {

    private static String FILE_PATH = "pingPongEloRatingID";
    private final int playerID;

    private static String FIND_ELO_RATINGS_FOR_PLAYER = "from PersistenceEloRating as ratings where ratings.playerID = :playerid";

    private final File file;

    private SessionFactory factory;

    public EloRatingPersistenceManager(String filePath,int playerID) {
        this.file = new File(filePath);
        this.playerID = playerID;
        try {
            Configuration config = new Configuration().configure();
            config.addAnnotatedClass(PersistenceEloRating.class);
            this.factory = config.buildSessionFactory();
        } catch (HibernateException e ) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public EloRatingPersistenceManager(int playerID) {
        String path = FILE_PATH+playerID+".txt";
        this.file = new File(path);
        this.playerID = playerID;
        try {
            Configuration config = new Configuration().configure();
            config.addAnnotatedClass(PersistenceEloRating.class);
            this.factory = config.buildSessionFactory();
        } catch (HibernateException e ) {
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
            session.flush();
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
            session.flush();
            session.close();
        } catch (NoResultException | HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }

    }

    public File getFile() {
        return file;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String readFile() {
        try{
            return this.getFile().readFile();
        } catch(NullPointerException e){
            return "No Players Found";
        }
    }

    public void writeEloRatingListToFile(PersistencePlayerEloRatingList ratings) {
        try {
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            ratings.setSortOrder();
            for (PersistenceEloRating rating: ratings.getList()) {
                session.saveOrUpdate(rating);
            }
            transaction.commit();
            session.flush();
            session.close();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public PersistencePlayerEloRatingList getEloRatingList() {
        try {
            Session session = factory.openSession();
            Query query = session.createQuery(FIND_ELO_RATINGS_FOR_PLAYER);
            query.setParameter("playerid", this.getPlayerID());
            List<PersistenceEloRating> ratings = query.list();
            Collections.sort(ratings, Comparator.comparing((PersistenceEloRating rating) -> rating.getGameID()));
            return new PersistencePlayerEloRatingList(new LinkedList<>(ratings));
        } catch (HibernateException e ) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public PersistenceEloRating getRatingByGameID(int gameID) {
        PersistencePlayerEloRatingList list = this.getEloRatingList();
        return list.getRating(list.getIndexOfGame(gameID));
    }

    public String writeEloRatingListToJson(PersistencePlayerEloRatingList ratings) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.writeValueAsString(ratings);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public void editEloRating(int gameID, double eloRating) {
        PersistencePlayerEloRatingList ratings = this.getEloRatingList();
        PersistenceEloRating rating = new PersistenceEloRating(eloRating,this.getPlayerID(),gameID);

        ratings.replaceEloRating(ratings.getIndexOfGame(gameID),rating);

        this.getFile().writeFile(this.writeEloRatingListToJson(ratings),false);
    }



}
