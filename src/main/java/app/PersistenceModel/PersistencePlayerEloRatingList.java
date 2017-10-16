package app.PersistenceModel;

import app.PersistenceManagers.GamePersistenceManager;
import app.ViewModel.EloRating;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
        this.setSortOrder();
    }

    public void addEloRating(int index, PersistenceEloRating rating) {
        this.eloRatingList.add(index, rating);
        this.setSortOrder();
    }

    public void replaceEloRating(int index, PersistenceEloRating rating) {
        PersistenceEloRating ratingToUpdate = this.eloRatingList.get(index);
        ratingToUpdate.setEloRating(rating.getEloRating());
        this.setSortOrder();
    }

    public void replaceEloRatingWithGameId(int gameId, PersistenceEloRating rating) {
        this.replaceEloRating(this.getIndexOfGame(gameId),rating);
    }

    public int getIndexOfGame(int gameID) {
        for(int i = 0; i< this.eloRatingList.size(); i++) {
            if(this.eloRatingList.get(i).getGameID() == gameID){
                return i;
            }
        }
        return 0;
    }

    public void insertNewRatingAtIndex(int index, PersistenceEloRating rating) {
        this.eloRatingList.add(index,rating);
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
        this.sortEloRatings();
        int i = 0;
        for (PersistenceEloRating rating: this.eloRatingList) {
            rating.setSortOrder(i);
            i +=1;
        }
    }

    private void sortEloRatings() {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PersistenceGame> gamesForPlayer = gPM.getGamesForPlayer(this.eloRatingList.get(0).getPlayerID());

        gamesForPlayer.sort(Comparator.comparing(g -> g.getTime()));

        LinkedList<PersistenceEloRating> sortedRatings = new LinkedList<>();

        sortedRatings.add(this.eloRatingList.get(this.getIndexOfGame(0)));

        for (PersistenceGame game: gamesForPlayer) {
            int index = this.getIndexOfGame(game.getiD());
            sortedRatings.add(this.eloRatingList.get(index));
        }

        this.eloRatingList = sortedRatings;
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
