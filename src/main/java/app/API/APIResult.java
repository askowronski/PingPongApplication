package app.API;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class APIResult {

    @JsonProperty("success") private  boolean success;
    @JsonProperty("message") private  String message;

    @JsonCreator
    public APIResult(@JsonProperty("success") boolean success, @JsonProperty("message") String message) {
         this.success=success;
        this.message=message;
    }

    public APIResult() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
