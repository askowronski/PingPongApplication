package app.PersistenceModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedList;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PersistencePlayerEloRatingList {

    @JsonProperty("eloRatingList") private LinkedList<PersistenceEloRating> eloRatingList;

    @JsonCreator
    public PersistencePlayerEloRatingList(@JsonProperty("eloRatingList") LinkedList<PersistenceEloRating> eloRatingList) {
        this.eloRatingList = eloRatingList;
    }

    @JsonCreator
    public PersistencePlayerEloRatingList() {
        this.eloRatingList = new LinkedList<>();
    }

    public void addEloRating(PersistenceEloRating rating) {
        this.eloRatingList.add(rating);
    }

    public void replaceEloRating(int index, PersistenceEloRating rating) {
        this.eloRatingList.set(index,rating);
    }

    public int getIndexOfGame(int gameID) {
        for(int i = 0; i< this.eloRatingList.size(); i++) {
            if(this.eloRatingList.get(i).getGameID() == gameID){
                return i;
            }
        }
        return -1;
    }

    public int getListSize() {
        return this.eloRatingList.size();
    }

    public PersistenceEloRating getRating(int index) {
        return this.eloRatingList.get(index);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof PersistencePlayerEloRatingList)){
            return false;
        }
        PersistencePlayerEloRatingList ratings = (PersistencePlayerEloRatingList) o;
        LinkedList<PersistenceEloRating> ratingList = ratings.eloRatingList;
        for(int i = 0; i<ratingList.size();i++){
            if(!ratingList.get(i).equals(this.eloRatingList.get(i))){
                return false;
            }
        }
        return true;
    }
}
