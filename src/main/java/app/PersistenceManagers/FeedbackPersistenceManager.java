package app.PersistenceManagers;

import app.Exceptions.InvalidParameterException;
import app.PersistenceModel.PersistenceFeedBack;
import app.ReadWriteFile.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.StringUtils;
import javax.persistence.NoResultException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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

    public PersistenceFeedBack createFeedback(PersistenceFeedBack feedBack) throws InvalidParameterException{
        if (StringUtils.isEmptyOrWhitespaceOnly(feedBack.getFeedBack())) {
            throw new InvalidParameterException("Please provide feedback.");
        }

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
