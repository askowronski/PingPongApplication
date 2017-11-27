package app.PersistenceManagers;

import app.PersistenceModel.PersistenceFeedBack;
import app.ReadWriteFile.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.NoResultException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Created by askowronski on 11/26/17.
 */
public class FeedbackPersistenceManager {

    private SessionFactory factory;
    public FeedbackPersistenceManager() {
        try {
            this.factory = HibernateConfiguration.SESSION_FACTORY;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public PersistenceFeedBack createFeedback(PersistenceFeedBack feedBack) {

        try {
            Session session = this.factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(feedBack);
            transaction.commit();
            session.close();

            return feedBack;
        } catch (HibernateException | NoResultException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public String writeFeedBackToJson(PersistenceFeedBack feedBack) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(feedBack);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

}
