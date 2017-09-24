package app.API.GraphData;


import app.ViewModel.PingPongGame;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class DateRangeData {

    @JsonProperty(value="beginningTime") public final Date beginningTime;
    @JsonProperty(value="endingTime") public final Date endingTime;

    @JsonCreator
    public DateRangeData(@JsonProperty(value="beginningTime") Date beginningTime,
            @JsonProperty(value="endingTime")Date endingTime) {
        this.beginningTime=beginningTime;
        this.endingTime=endingTime;
    }
}
