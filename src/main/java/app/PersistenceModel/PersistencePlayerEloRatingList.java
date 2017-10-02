package app.PersistenceModel;

import app.ViewModel.EloRating;
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
        this.setSortOrder();
    }

    @JsonCreator
    public PersistencePlayerEloRatingList() {
        this.eloRatingList = new LinkedList<>();
    }

    public void addEloRating(PersistenceEloRating rating) {
        this.eloRatingList.add(rating);
        this.setSortOrder();
    }

    public void replaceEloRating(int index, PersistenceEloRating rating) {
        this.eloRatingList.set(index,rating);
        this.setSortOrder();
    }

    public int getIndexOfGame(int gameID) {
        for(int i = 0; i< this.eloRatingList.size(); i++) {
            if(this.eloRatingList.get(i).getGameID() == gameID){
                return i;
            }
        }
        return eloRatingList.size()-1;
    }

    public int getListSize() {
        return this.eloRatingList.size();
    }

    public PersistenceEloRating getRating(int index) {
        return this.eloRatingList.get(index);
    }

    public boolean deleteRating(PersistenceEloRating rating) {
        boolean check = this.eloRatingList.remove(rating);
        this.setSortOrder();
        return check;
    }

    public LinkedList<PersistenceEloRating> getList() {
        return this.eloRatingList;
    }

    public void setSortOrder() {
        int i = 1;
        for (PersistenceEloRating rating: this.eloRatingList) {
            rating.setSortOrder(i);
            i++;
        }
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
