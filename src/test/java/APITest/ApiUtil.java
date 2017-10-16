package APITest;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public class ApiUtil {
    public static String retrieveMessage(HttpResponse response) throws IOException {
        String jsonFromResponse = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(jsonFromResponse);
        String messsageData = data.path("message").asText();
        return messsageData;
    }
}
