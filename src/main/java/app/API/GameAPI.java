package app.API;

import app.PersistenceManagers.GamePersistenceManager;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.StatsEngine.PingPongGame;
import app.StatsEngine.Player;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class GameAPI {


    public void createAndSaveGame() {

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(method=POST,path="/CreateGame")
    public APIResult processCreateGame(@RequestParam(value="player1ID",required = true) String player1ID,
                                  @RequestParam(value="player2ID",required = true) String player2ID,
                                  @RequestParam(value="score1",required = true) String score1,
                                  @RequestParam(value="score2",required = true) String score2) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();

        try {
            int IDplayer1 = Integer.parseInt(player1ID);
            int IDplayer2 = Integer.parseInt(player2ID);

            int player1Score = Integer.parseInt(score1);
            int player2Score = Integer.parseInt(score2);

            Player player1 = gPM.getPlayer(IDplayer1);
            Player player2 = gPM.getPlayer(IDplayer2);

            List<Player> players = pPM.getPlayers();
            int indexPlayer1 = players.indexOf(player1);
            int indexPlayer2 = players.indexOf(player2);
            if(player1Score>player2Score) {

            } else {

            }

            PingPongGame game = new PingPongGame(gPM.getNextID(),player1,player2,player1Score,player2Score);
            gPM.writeGameToFile(game);
            return  new APIResult(true,"Game Created");

        } catch (NumberFormatException e ){
            e.printStackTrace();
            return new APIResult(false,"Game Unsuccessfully Created");
        }

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(method=POST,path="/EditGame")
    public APIResult processEditGame(@RequestParam(value="iD",required=true) int gameID,
                                @RequestParam(value="player1ID",required = false) Optional<String> player1Username,
                                @RequestParam(value="player2ID",required = false) Optional<String> player2Username,
                                @RequestParam(value="score1",required = false) Optional<Integer> score1,
                                @RequestParam(value="score2",required = false) Optional<Integer> score2){
        GamePersistenceManager gPM = new GamePersistenceManager();
        PlayerPersistenceManager pPM = new PlayerPersistenceManager();
        PingPongGame game = gPM.getGameByID(gameID);
        Player newPlayer1;
        Player newPlayer2;
        int newScore1;
        int newScore2;

        if(game.getiD()!=0){
            if(player1Username.isPresent()){
                newPlayer1=pPM.getPlayerByUsername(player1Username.get());
            } else {
                newPlayer1 = game.getPlayer1();
            }

            if(player2Username.isPresent()){
                newPlayer2=pPM.getPlayerByUsername(player2Username.get());
            } else {
                newPlayer2 = game.getPlayer2();
            }

            if(score1.isPresent()){
                newScore1=score1.get();
            } else {
                newScore1 = game.getPlayer1Score();
            }

            if(score2.isPresent()){
                newScore2=score2.get();
            } else {
                newScore2 = game.getPlayer2Score();
            }

            gPM.editWriteGameToFile(game,newPlayer1,newPlayer2,newScore1,newScore2);

            return new APIResult(true,"Game Edited");
        }
            return new APIResult(false,"Game Unsuccessfully Edited");

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(path = "/DeleteGame",method=POST)
    public APIResult processDeleteGame(@RequestParam("iD") int iD) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PingPongGame game = gPM.getGameByID(iD);
        if(game.getiD()==0){
            return new APIResult(false,"Game Not Found");
        }
        List<PingPongGame> games = gPM.getGames();
        games.remove(game);
        gPM.writeGamesToFile(games);
        return new APIResult(true,"Game Deleted");
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(path = "/GetGame", method=GET)
    public APIResult processGetGame(@RequestParam("iD") int iD) {
        GamePersistenceManager gPM = new GamePersistenceManager();
        PingPongGame game = gPM.getGameByID(iD);
        return new APIResult(true, gPM.writeGameToJson(game));

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(path = "/GetGames", method=GET)
    public APIResult processGetGame() {
        GamePersistenceManager gPM = new GamePersistenceManager();
        List<PingPongGame> games = gPM.getGames();
        return new APIResult(true, gPM.writeGameToGamesJson(games));

    }


}
