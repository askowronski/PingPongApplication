package app.API;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import app.PersistenceManagers.FeedbackPersistenceManager;
import app.PersistenceModel.PersistenceFeedBack;
import javax.persistence.NoResultException;
import org.hibernate.HibernateException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin()
@RestController
public class FeedBackApi {



    @CrossOrigin
    @RequestMapping(method=POST,path="/CreateFeedback")
    public APIResult createFeedback(@RequestBody PersistenceFeedBack feedBack) {

        try {
            FeedbackPersistenceManager fPM = new FeedbackPersistenceManager();
            fPM.createFeedback(feedBack);
            return new APIResult(true,fPM.writeFeedBackToJson(feedBack));

        } catch(HibernateException | NoResultException e) {
            return new APIResult(false, e.getMessage());
        }


    }
}
