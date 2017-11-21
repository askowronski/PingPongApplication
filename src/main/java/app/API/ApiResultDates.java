package app.API;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiResultDates extends APIResult {


    @JsonProperty("beginningDate")
    private String beginningDate;
    @JsonProperty("endingDate")
    private String endingDate;

    @JsonCreator
    public ApiResultDates(Boolean result, @JsonProperty("beginningDate") String beginningDate,
            @JsonProperty("endingDate") String endingDate) {
        super();
        this.setSuccess(true);
        this.beginningDate = beginningDate;
        this.endingDate = endingDate;
        this.setMessage("{\"beginningDate\":"+beginningDate+",\"endingDate\":"+endingDate+"}");

    }

}


