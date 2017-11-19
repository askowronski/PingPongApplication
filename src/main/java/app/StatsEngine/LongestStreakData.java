package app.StatsEngine;

import app.ViewModel.Player;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LongestStreakData {


    @JsonProperty("player")
    private Player player;
    @JsonProperty("streak")
    private int streak;

    @JsonCreator
    public LongestStreakData( @JsonProperty("player") Player player, @JsonProperty("streak") int streak) {
        this.player = player;
        this.streak = streak;
    }

    public String writeDataToJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
