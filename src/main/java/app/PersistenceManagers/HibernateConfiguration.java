package app.PersistenceManagers;

import app.PersistenceModel.PersistenceEloRating;
import app.PersistenceModel.PersistenceFeedBack;
import app.PersistenceModel.PersistenceGame;
import app.PersistenceModel.PersistencePlayer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class HibernateConfiguration {

    public static final Configuration CONFIG = new Configuration().configure().addAnnotatedClass(
            PersistenceEloRating.class).addAnnotatedClass(PersistenceGame.class).addAnnotatedClass(
            PersistencePlayer.class).addAnnotatedClass(PersistenceFeedBack.class);

    private static final StandardServiceRegistryBuilder ssrb =  new StandardServiceRegistryBuilder().applySettings(CONFIG.getProperties());

    public static final SessionFactory SESSION_FACTORY =  CONFIG.buildSessionFactory(ssrb.build());

    public static Session SESSION = SESSION_FACTORY.openSession();


    public static void closeSession() {
        SESSION.close();
    }

    public static void openSession() {
        if (SESSION.isOpen()) {
            closeSession();
        }
        SESSION = SESSION_FACTORY.openSession();
    }



}
