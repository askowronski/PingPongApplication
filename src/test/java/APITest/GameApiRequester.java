package APITest;

import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class GameApiRequester {

    public static String HOST = "http://localhost:8080/";

    public List<PingPongGame> getGames() throws IOException {
        HttpUriRequest getGames = new HttpGet(
                HOST + "GetGames");
        HttpResponse response = HttpClientBuilder.create().build()
                .execute(getGames);

        String jsonFromResponse = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(jsonFromResponse);
        String messsageData = data.path("message").asText();
        TypeReference<List<PingPongGame>> mapType;
        mapType = new TypeReference<List<PingPongGame>>() {
        };
        return mapper.readValue(messsageData, mapType);
    }

    public HttpResponse getGame(int gameId) throws  IOException {
        HttpUriRequest getGames = new HttpGet(
                HOST + "GetGame?iD="+gameId);
        return HttpClientBuilder.create().build()
                .execute(getGames);

    }

    public PingPongGame getGame(HttpResponse response) throws  IOException {
        String jsonFromResponse = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(jsonFromResponse);
        String messsageData = data.path("message").asText();
        TypeReference<PingPongGame> mapType;
        mapType = new TypeReference<PingPongGame>() {
        };
        return mapper.readValue(messsageData, mapType);
    }

    public List<PingPongGame> getGamesForPlayer(int id) throws IOException {
        HttpUriRequest getGames = new HttpGet(
                HOST + "GetGamesForPlayer?id=" + id);
        HttpResponse response = HttpClientBuilder.create().build()
                .execute(getGames);

        String jsonFromResponse = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(jsonFromResponse);
        String messsageData = data.path("message").asText();
        TypeReference<List<PingPongGame>> mapType;
        mapType = new TypeReference<List<PingPongGame>>() {
        };
        return mapper.readValue(messsageData, mapType);
    }

    public HttpResponse createGame(int player1Id, int player2Id, int score1, int score2)
            throws IOException {
        HttpUriRequest getGames = new HttpPost(
                HOST + "CreateGame?player1ID=" + player1Id + "&player2ID=" + player2Id
                        + "&score1=" + score1 + "&score2=" + score2 + "&date=2017OCT10&time=00:00:00");
        return HttpClientBuilder.create().build()
                .execute(getGames);
    }

    public HttpResponse createGame(int player1Id, int player2Id, int score1, int score2,
            String dateString)
            throws IOException {
        HttpUriRequest getGames = new HttpPost(
                HOST + "CreateGame?player1ID=" + player1Id + "&player2ID=" + player2Id
                        + "&score1=" + score1 + "&score2=" + score2 + "&date=" + dateString+"&time=00:00:00");
        return HttpClientBuilder.create().build()
                .execute(getGames);
    }


    public HttpResponse deleteGame(int id) throws IOException {
        HttpUriRequest getGames = new HttpDelete(
                HOST + "HardDeleteGame?iD=" + id);
        return HttpClientBuilder.create().build()
                .execute(getGames);
    }

    public HttpResponse editGame(int id, int player1Id) throws IOException {
        HttpUriRequest getGames = new HttpPost(
                HOST + "EditGame?iD=" + id + "&player1ID=" + player1Id);
        return HttpClientBuilder.create().build()
                .execute(getGames);
    }

    public HttpResponse editGamePlayer2(int id, int player1Id) throws IOException {
        HttpUriRequest getGames = new HttpPost(
                HOST + "EditGame?iD=" + id + "&player2ID=" + player1Id);
        return HttpClientBuilder.create().build()
                .execute(getGames);
    }

    public HttpResponse editGame(int id, int player1Id, int player2Id) throws IOException {
        HttpUriRequest getGames = new HttpPost(
                HOST + "EditGame?iD=" + id + "&player1ID=" + player1Id + "&player2ID=" + player2Id);
        return HttpClientBuilder.create().build()
                .execute(getGames);
    }

    public HttpResponse editGameScore1(int id, int score1) throws IOException {
        HttpUriRequest getGames = new HttpPost(
                HOST + "EditGame?iD=" + id + "&score1=" + score1);
        return HttpClientBuilder.create().build()
                .execute(getGames);
    }

    public HttpResponse editGame(int gameId, String date) throws IOException {
        HttpUriRequest getGames = new HttpPost(
                HOST + "EditGame?iD=" + gameId + "&time=" + date+"+00:00");
        return HttpClientBuilder.create().build()
                .execute(getGames);
    }


}
