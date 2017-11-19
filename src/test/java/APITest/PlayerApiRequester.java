package APITest;


import app.ViewModel.Player;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class PlayerApiRequester {

    public static String HOST = "http://localhost:8080/";

    public PlayerApiRequester() {

    }

    public HttpResponse createPlayer(String username, String firstName, String lastName)
            throws IOException {
        HttpUriRequest request = new HttpPost(
                HOST + "CreatePlayer?username=" + username + "&firstName=" + firstName
                        + "&lastName=" + lastName);
        return HttpClientBuilder.create().build().execute(request);
    }

    public HttpResponse editPlayer(int id, String username) throws IOException {
        HttpUriRequest request = new HttpPost(
                HOST + "EditPlayer?id=" + id + "&newUsername=" + username);
        return HttpClientBuilder.create().build().execute(request);
    }

    public HttpResponse deletePlayer(int id) throws IOException {
        HttpUriRequest request = new HttpDelete(HOST + "DeletePlayer?id=" + id);
        return HttpClientBuilder.create().build().execute(request);
    }

    public HttpResponse hardDeletePlayer(int id) throws IOException {
        HttpUriRequest deleteRequest = new HttpDelete(
                HOST + "HardDeletePlayer?id=" + id);
        return HttpClientBuilder.create().build()
                .execute(deleteRequest);
    }

    public HttpResponse getPlayers() throws IOException {
        HttpUriRequest getPlayers = new HttpGet(
                HOST + "/GetPlayers");
        return HttpClientBuilder.create().build()
                .execute(getPlayers);
    }

    public Player getPlayer(int id) throws IOException {
        HttpUriRequest getPlayer = new HttpGet(
                HOST + "GetPlayer?id=" + id);
        HttpResponse response = HttpClientBuilder.create().build()
                .execute(getPlayer);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(EntityUtils.toString(response.getEntity()));
        String message = data.path("message").toString();
        JsonNode playerNode = mapper.readTree(message);

        return mapper.readValue(playerNode.asText(), Player.class);
    }

    public Player getPlayer(String username) throws IOException {
        HttpUriRequest getPlayer = new HttpGet(HOST + "GetPlayerByUsername?username=" + username);
        HttpResponse response = HttpClientBuilder.create().build().execute(getPlayer);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(EntityUtils.toString(response.getEntity()));
        String message = data.path("message").toString();
        JsonNode playerNode = mapper.readTree(message);

        return mapper.readValue(playerNode.asText(), Player.class);
    }

}
