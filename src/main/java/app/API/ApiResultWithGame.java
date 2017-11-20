package app.API;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResultWithGame extends APIResult {

    @JsonProperty("game")
    private String game;

    public ApiResultWithGame(Boolean result, String message, @JsonProperty("game") String game) {
        super(result,message);
        this.game = game;
    }
}
