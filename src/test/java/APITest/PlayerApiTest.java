package APITest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.ViewModel.Player;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class PlayerApiTest {

    public static List<String> usersToDelete = new ArrayList<>();
    public static PlayerApiRequester api = new PlayerApiRequester();

    @BeforeEach
    public  void beforeEach() throws IOException {
        HttpResponse response = api.createPlayer("swag");

        usersToDelete.add("swag");
    }

    @AfterEach
    public  void afterEach() throws IOException {
        HttpResponse response = api.getPlayers();

        List<Player> players = retrievePlayerListResourceFromResponse(response);

        for (Player player : players) {
            if (usersToDelete.contains(player.getUsername())) {
                api.hardDeletePlayer(player.getiD());
            }
        }

        usersToDelete.clear();
    }

    @Test
    public void testCreatePlayer() throws IOException {
        String username = "swag2";
        usersToDelete.add(username);

        HttpResponse response = api.createPlayer(username);

        Player player = api.getPlayer(username);
        assertEquals(username, player.getUsername());
    }

    @Test
    public void testCreatePlayerWithTakenUsername() throws IOException {
        String username = usersToDelete.get(0);

        HttpResponse response = api.createPlayer(username);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(EntityUtils.toString(response.getEntity()));
        String message = data.path("message").asText();
        assertEquals(message, "Username is either taken or blank.");
    }

    @Test
    public void testEditPlayer() throws IOException {
        String newUsername = "swaggggggggggggg";
        Player player = api.getPlayer(usersToDelete.get(0));
        HttpResponse response = api.editPlayer(player.getiD(), newUsername);
        Player playerAfter = api.getPlayer(player.getiD());

        assertEquals(playerAfter.getUsername(), newUsername);
    }

    @Test
    public void testPlayersExist() throws IOException {

        HttpResponse response = api.getPlayers();

        List<Player> players = retrievePlayerListResourceFromResponse(response);

        assertTrue(players.size() > 1);
    }

    public static <T> T retrieveResourceFromResponse(HttpResponse response, Class<T> clazz)
            throws IOException {
        String jsonFromResponse = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(jsonFromResponse, clazz);
    }

    public static List<Player> retrievePlayerListResourceFromResponse(HttpResponse response)
            throws IOException {
        String jsonFromResponse = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(jsonFromResponse);
        String messsageData = data.path("message").asText();
        TypeReference<List<Player>> mapType;
        mapType = new TypeReference<List<Player>>() {
        };
        return mapper.readValue(messsageData, mapType);
    }


}