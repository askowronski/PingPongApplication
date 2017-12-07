package app.PersistenceManagers;

import app.PersistenceModel.PersistenceEloRating;
import app.PersistenceModel.PersistenceGame;
import app.PersistenceModel.PersistencePlayer;
import app.PersistenceModel.PersistencePlayerEloRatingList;
import app.ViewModel.EloRating;
import app.ViewModel.GameOutcomeEnum;
import app.ViewModel.PingPongGame;
import app.ViewModel.Player;
import app.ReadWriteFile.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mysql.cj.core.util.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;


public class PlayerPersistenceManager {

    public static String FIND_ALL_PLAYERS = "from PersistencePlayer as player where player.deleted = 0";
    public static String FIND_ALL_PLAYERS_INCLUDING_DELETED = "from PersistencePlayer";
    public static String FIND_PLAYER_BY_ID = "from PersistencePlayer as player where player.id = :id";
    public static String FIND_PLAYER_BY_USERNAME = "from PersistencePlayer as player where player.username = :username";


    private SessionFactory factory;

    public PlayerPersistenceManager() {
        try {
            this.factory = HibernateConfiguration.SESSION_FACTORY;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }

    }

    public PlayerPersistenceManager(String filePath) {
        try {
            Configuration config = HibernateConfiguration.CONFIG;
            this.factory = HibernateConfiguration.SESSION_FACTORY;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    // Create

    public void createPlayer(String username, String firstName, String lastName) {
        if (StringUtils.isEmptyOrWhitespaceOnly(username)) {
            throw new IllegalArgumentException("Username is either taken or blank.");
        }

        if (this.checkUsernameValidity(username)) {
            try {
                PersistencePlayer player = new PersistencePlayer();
                player.setUsername(username);
                player.setFirstName(firstName);
                player.setLastName(lastName);
                Session session = this.factory.openSession();
                Transaction transaction = session.beginTransaction();
                session.save(player);
                transaction.commit();
                session.close();
                EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(player.getId());
                eRPM.createEloRating(new PersistenceEloRating(EloRating.DEFAULT_RATING, player.getId(), 0));
            } catch (HibernateException he) {
                System.out.println(he.getMessage());
                throw he;
            }
        } else {
            throw new IllegalArgumentException("Username is either taken or blank.");
        }
    }

    // Read

    public PersistencePlayer getPlayerById(int id) {
        try {
            Session session = factory.openSession();
            Query query = session.createQuery(FIND_PLAYER_BY_ID);
            query.setParameter("id", id);
            Object obj = query.getSingleResult();
            session.close();
            return (PersistencePlayer) obj;
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            throw new NoResultException("No Player found with ID " + id + ".");
        }
    }

    public PersistencePlayer getPlayerByUsername(String username) {
        try {
            PersistencePlayer player;
            Session session = factory.openSession();
            Query query = session.createQuery(FIND_PLAYER_BY_USERNAME);
            query.setParameter("username", username);
            Object obj = query.getSingleResult();
            session.close();
            return (PersistencePlayer) obj;
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public List<Player> getViewPlayers() {
        List<PersistencePlayer> players = this.getPlayersNew();
        List<Player> viewPlayers = new ArrayList<>();

        for (PersistencePlayer player : players) {
            EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(player.getId());
            viewPlayers.add(new Player(
                    new EloRating(eRPM.getPlayersLastRating().getEloRating())
                    , player.getId(), player.getUsername(), player.getFirstName(),
                    player.getLastName()));
        }
        return viewPlayers;
    }

    public List<PersistencePlayer> getPlayersNew() {
        List<PersistencePlayer> players = new ArrayList<>();
        try {
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery(FIND_ALL_PLAYERS);
            players = query.getResultList();
            session.close();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return players;
    }

    public List<PersistencePlayer> getPlayersIncludingDeleted() {
        List<PersistencePlayer> players = new ArrayList<>();
        try {
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery(FIND_ALL_PLAYERS_INCLUDING_DELETED);
            players = query.getResultList();
            session.close();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return players;
    }

    // Update

    public void updatePlayer(PersistencePlayer player) {
        try {
            Session session = this.factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();
            session.close();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    public boolean updatePlayer(int id, String newUsername, String newFirstName, String newLastName) {
        try {
            PersistencePlayer player = this.getPlayerById(id);

            if (player.getId() == 0) {
                throw new IllegalArgumentException("Id is not valid.");
            }

            if (StringUtils.isEmptyOrWhitespaceOnly(newUsername)) {
                throw new IllegalArgumentException("Username must not be blank.");
            }

            if (newUsername != null) {
                player.setUsername(newUsername);
                if (!this.checkUsernameValidity(newUsername)) {
                    throw new IllegalArgumentException("Username is taken.");
                }
            }
            if (newFirstName != null) {
                player.setFirstName(newFirstName);
            }
            if (newLastName != null) {
                player.setLastName(newLastName);
            }


            try {
                this.updatePlayer(player);
                return true;
            } catch (HibernateException e) {
                System.out.println(e.getMessage());
                throw e;
            }
        } catch (NoResultException e) {
            throw new IllegalArgumentException("No player found with ID " + id + ".");
        }

    }

    public void updatePlayers(List<PersistencePlayer> players) {
        try {
            Session session = this.factory.openSession();
            Transaction transaction = session.beginTransaction();
            for (PersistencePlayer player : players) {
                session.update(player);
            }
            transaction.commit();
            session.close();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    public String writePlayerToJson(Player player) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(player);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public boolean checkUsernameValidity(String username) {
        List<PersistencePlayer> players = this.getPlayersIncludingDeleted();
        List<String> usernames = new ArrayList<>();

        if (StringUtils.isEmptyOrWhitespaceOnly(username)) {
            return false;
        }

        for (PersistencePlayer player : players) {
            usernames.add(player.getUsername());
        }
        return !usernames.contains(username);
    }

    public String writePlayerArrayToJson(List<Player> players) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(players);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public List<Player> readPlayerArrayFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Player>> mapType;
        mapType = new TypeReference<List<Player>>() {
        };
        try {
            List<Player> players = mapper.readValue(json, mapType);
            return players;
        } catch (IOException e) {
            List<Player> players = new ArrayList<Player>();
            e.printStackTrace();
            return players;
        }
    }

    public Player getViewPlayerByID(int id, int gameID) {
        EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(id);
        PersistencePlayer player = this.getPlayerById(id);
        PersistenceEloRating rating = eRPM.getRatingByGameID(gameID);
        return new Player(new EloRating(rating.getEloRating()), id, player.getUsername(),
                player.getFirstName(), player.getLastName());
    }

    public Player getViewPlayerByUsername(String username, int gameID) {
        PersistencePlayer player = this.getPlayerByUsername(username);
        EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(player.getId());
        PersistenceEloRating rating = eRPM.getRatingByGameID(gameID);
        return new Player(new EloRating(rating.getEloRating()), player.getId(),
                player.getUsername(), player.getFirstName(), player.getLastName());
    }

    public boolean deletePlayerById(int id) {
        try {
            PersistencePlayer player = this.getPlayerById(id);
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            player.setDeleted(1);
            session.update(player);
            transaction.commit();
            session.close();
            return true;
        } catch (HibernateException | NoResultException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public boolean hardDeletePlayerById(int id) {
        try {
            PersistencePlayer player = this.getPlayerById(id);
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(id);
            eRPM.hardDeleteEloRatings();
            session.delete(player);
            transaction.commit();
            session.close();
            return true;
        } catch (HibernateException | NoResultException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }


    public boolean deletePlayerByUsername(String username) {
        try {
            PersistencePlayer player = this.getPlayerByUsername(username);
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(player.getId());
            eRPM.hardDeleteEloRatings();
            player.setDeleted(1);
            session.update(player);
            transaction.commit();
            session.close();
            return true;
        } catch (HibernateException | NoResultException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public boolean hardDeletePlayerByUsername(String username) {
        try {
            PersistencePlayer player = this.getPlayerByUsername(username);
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(player);
            transaction.commit();
            session.close();
            return true;
        } catch (HibernateException | NoResultException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }


    public Player getPlayerWithHighestRating() {

        List<Player> players = this.getViewPlayers();

        if (players.size() == 0) {
            throw new NoResultException("No Players Found.");
        }
        Player highestPlayer = players.get(0);
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getRating().getRating() > highestPlayer.getRating()
                    .getRating()) {
                highestPlayer = players.get(i);
            }
        }
        return highestPlayer;
    }

    public void updatePlayersEloRatingEdit(PersistenceGame game, PersistenceGame oldGame,
            GamePersistenceManager gPM, boolean delete) {
        List<PersistenceGame> games = gPM.getGamesNew();

        int indexOfGame = games.indexOf(game);

        boolean editPlayer1 = false;
        boolean editPlayer2 = false;
        if (oldGame.getPlayer1ID() != game.getPlayer1ID() &&
                oldGame.getPlayer1ID() != game.getPlayer2ID()) {
            EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(
                    oldGame.getPlayer1ID());
            eRPM.deleteEloRating(game.getiD());
            editPlayer1 = true;
        }

        if (oldGame.getPlayer2ID() != game.getPlayer1ID() &&
                oldGame.getPlayer2ID() != game.getPlayer2ID()) {
            EloRatingPersistenceManager eRPM = new EloRatingPersistenceManager(
                    oldGame.getPlayer2ID());
            eRPM.deleteEloRating(game.getiD());
            editPlayer2 = true;
        }
        if (indexOfGame >= 0 || game.equals(oldGame)) {
            for (int i = indexOfGame; i < games.size(); i++) {

                EloRatingPersistenceManager eRPM1 = new EloRatingPersistenceManager(
                        games.get(i).getPlayer1ID());
                EloRatingPersistenceManager eRPM2 = new EloRatingPersistenceManager(
                        games.get(i).getPlayer2ID());
                PersistencePlayerEloRatingList listPlayer1 = eRPM1.getEloRatingList();
                PersistencePlayerEloRatingList listPlayer2 = eRPM2.getEloRatingList();
                PersistenceGame currentGame = games.get(i);
                if (delete == true && indexOfGame == games.size() - 1) {
                    break;
                }

                int indexOfGame1 = listPlayer1.getIndexOfGame(currentGame.getiD());
                int indexOfGame2 = listPlayer2.getIndexOfGame(currentGame.getiD());
                PersistenceEloRating newRating1;
                PersistenceEloRating newRating2;
                int indexOfElo1;
                int indexOfElo2;
                if (indexOfGame1 <= 0) {
                    indexOfElo1 = listPlayer1.getListSize() - 1;
                } else {
                    indexOfElo1 = indexOfGame1 - 1;
                }
                if (indexOfGame2 <= 0) {
                    indexOfElo2 = listPlayer2.getListSize() - 1;
                } else {
                    indexOfElo2 = indexOfGame2 - 1;
                }

                newRating1 = this.getNewRating(currentGame,
                        currentGame.getPlayer1ID(),
                        listPlayer1.getRating(indexOfElo1),
                        listPlayer2.getRating(indexOfElo2));

                newRating2 = this.getNewRating(currentGame,
                        currentGame.getPlayer2ID(),
                        listPlayer2.getRating(indexOfElo2),
                        listPlayer1.getRating(indexOfElo1));

                if (indexOfGame1 <= 0 || editPlayer1) {
                    listPlayer1.addEloRating(newRating1);
                    editPlayer1 = false;
                } else {
                    listPlayer1.replaceEloRating(indexOfGame1, newRating1);
                }
                if (indexOfGame2 <= 0 || editPlayer2) {
                    listPlayer2.addEloRating(newRating2);
                    editPlayer2 = false;
                } else {
                    listPlayer2.replaceEloRating(indexOfGame2, newRating2);
                }

                eRPM1.saveOrUpdateEloRating(newRating1);
                eRPM2.saveOrUpdateEloRating(newRating2);
            }
        }
    }

    public PersistenceEloRating getNewRating(PersistenceGame game,
            int player1ID,
            PersistenceEloRating rating,
            PersistenceEloRating oppRating) {
        if (game.getPlayer1ID() == player1ID) {
            if (game.getPlayer1Score() > game.getPlayer2Score()) {
                return rating.newRating(GameOutcomeEnum.WIN, oppRating, game.getiD());
            } else if (game.getPlayer1Score() < game.getPlayer2Score()) {
                return rating.newRating(GameOutcomeEnum.LOSS, oppRating, game.getiD());
            } else {
                return rating.newRating(GameOutcomeEnum.DRAW, oppRating, game.getiD());
            }
        } else {
            if (game.getPlayer2Score() > game.getPlayer1Score()) {
                return rating.newRating(GameOutcomeEnum.WIN, oppRating, game.getiD());
            } else if (game.getPlayer2Score() < game.getPlayer1Score()) {
                return rating.newRating(GameOutcomeEnum.LOSS, oppRating, game.getiD());
            } else {
                return rating.newRating(GameOutcomeEnum.DRAW, oppRating, game.getiD());
            }
        }
    }
}
