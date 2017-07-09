package app.API;

import app.PersistenceManagers.GamePersistenceManager;
import app.StatsEngine.PingPongGame;
import app.StatsEngine.Player;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class GameAPI {


    public void createAndSaveGame() {

    }

    @RequestMapping(method=POST,path="/CreateGame")
    public APIResult processCreateGame(@RequestParam(value="player1ID",required = true) String player1ID,
                                  @RequestParam(value="player2ID",required = true) String player2ID,
                                  @RequestParam(value="score1",required = true) String score1,
                                  @RequestParam(value="score2",required = true) String score2) {
        GamePersistenceManager gPM = new GamePersistenceManager();

        try {
            int IDplayer1 = Integer.parseInt(player1ID);
            int IDplayer2 = Integer.parseInt(player2ID);

            int player1Score = Integer.parseInt(score1);
            int player2Score = Integer.parseInt(score2);

            Player player1 = gPM.getPlayer(IDplayer1);
            Player player2 = gPM.getPlayer(IDplayer2);

            PingPongGame game = new PingPongGame(gPM.getNextID(),player1,player2,player1Score,player2Score);
            gPM.writeGameToFile(game);
            return  new APIResult(true,"Game Created");

        } catch (NumberFormatException e ){
            e.printStackTrace();
            return new APIResult(false,"Game Unsuccessfully Created");
        }

    }

    @RequestMapping(method=POST,path="/EditGame")
    public APIResult processEditGame(@RequestParam(value="iD",required=true) int gameID,
                                @RequestParam(value="player1ID",required = false) Optional<Integer> player1ID,
                                @RequestParam(value="player2ID",required = false) Optional<Integer> player2ID,
                                @RequestParam(value="score1",required = false) Optional<Integer> score1,
                                @RequestParam(value="score2",required = false) Optional<Integer> score2){
        GamePersistenceManager gPM = new GamePersistenceManager();
        PingPongGame game = gPM.getGameByID(gameID);
        Player newPlayer1;
        Player newPlayer2;
        int newScore1;
        int newScore2;

        if(game.getiD()!=0){
            if(player1ID.isPresent()){
                newPlayer1=gPM.getPlayer(player1ID.get());
            } else {
                newPlayer1 = game.getPlayer1();
            }

            if(player2ID.isPresent()){
                newPlayer2=gPM.getPlayer(player2ID.get());
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

            PingPongGame newGame = new PingPongGame(game.getiD(),newPlayer1,newPlayer2,newScore1,newScore2,game.getTime());
            gPM.editWriteGameToFile(game,newGame);

            return new APIResult(true,"Game Edited");
        }
            return new APIResult(false,"Game Unsuccessfully Edited");

    }

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


}
