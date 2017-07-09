package app.API;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class APIResult {

    @JsonProperty("success") private final boolean success;
    @JsonProperty("message") private final String message;

    @JsonCreator
    public APIResult(@JsonProperty("success") boolean success, @JsonProperty("message") String message) {
         this.success=success;
        this.message=message;
    }
}
