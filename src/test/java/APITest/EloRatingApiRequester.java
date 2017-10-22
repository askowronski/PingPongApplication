package APITest;

import app.PersistenceModel.PersistenceEloRating;
import app.ViewModel.EloRating;
import app.ViewModel.PingPongGame;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class EloRatingApiRequester {
    public static String HOST = "http://localhost:8080/";


    public List<PersistenceEloRating> getRatings(int playerId) throws IOException {
        HttpUriRequest getRatings = new HttpGet(
                HOST + "GetEloRatingsTest?playerId="+playerId);
        HttpResponse response = HttpClientBuilder.create().build()
                .execute(getRatings);

        String jsonFromResponse = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(jsonFromResponse);
        String messsageData = data.path("message").asText();
        TypeReference<List<PersistenceEloRating>> mapType;
        mapType = new TypeReference<List<PersistenceEloRating>>() {
        };
        return mapper.readValue(messsageData, mapType);
    }

}
