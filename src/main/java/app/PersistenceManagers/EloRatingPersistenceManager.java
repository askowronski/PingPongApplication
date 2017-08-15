package app.PersistenceManagers;


import app.PersistenceModel.PersistenceEloRating;
import app.PersistenceModel.PersistencePlayer;
import app.PersistenceModel.PersistencePlayerEloRatingList;
import app.ReadWriteFile.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EloRatingPersistenceManager {

    private static String FILE_PATH = "pingPongEloRatingID";
    private final int playerID;

    private final File file;

    public EloRatingPersistenceManager(String filePath,int playerID) {
        this.file = new File(filePath);
        this.playerID = playerID;
    }

    public EloRatingPersistenceManager(int playerID) {
        String path = FILE_PATH+playerID+".txt";
        this.file = new File(path);
        this.playerID = playerID;
    }

    public void writeEloRatingToFile(PersistenceEloRating eloRating, int playerID) {
        PersistencePlayerEloRatingList ratings = this.getEloRatingList();
        ratings.addEloRating(eloRating);
        this.getFile().writeFile(this.writeEloRatingListToJson(ratings),false);

    }

    public File getFile() {
        return file;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String readFile() {
        try{
            return this.getFile().readFile();
        } catch(NullPointerException e){
            return "No Players Found";
        }
    }

    public PersistencePlayerEloRatingList getEloRatingList() {
        ObjectMapper mapper = new ObjectMapper();
        String json = this.readFile();

        TypeReference<PersistencePlayerEloRatingList> mapType;
        mapType = new TypeReference<PersistencePlayerEloRatingList>(){};
        try {
            PersistencePlayerEloRatingList ratings = mapper.readValue(json, mapType);
            return ratings;
        } catch(IOException e){
            PersistencePlayerEloRatingList ratings = new PersistencePlayerEloRatingList(new LinkedList<PersistenceEloRating>());
            return ratings;
        }
    }

    public String writeEloRatingListToJson(PersistencePlayerEloRatingList ratings) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.writeValueAsString(ratings);
        } catch(JsonProcessingException e){
            return e.getMessage();
        }
    }

    public void editEloRating(int gameID, double eloRating) {
        PersistencePlayerEloRatingList ratings = this.getEloRatingList();
        PersistenceEloRating rating = new PersistenceEloRating(eloRating,this.getPlayerID(),gameID);

        ratings.replaceEloRating(ratings.getIndexOfGame(gameID),rating);

        this.getFile().writeFile(this.writeEloRatingListToJson(ratings),false);
    }



}
