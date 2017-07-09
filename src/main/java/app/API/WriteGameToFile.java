package app.API;


import app.EloRating;
import app.PersistenceManagers.PlayerPersistenceManager;
import app.Player;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class WriteGameToFile {

    public static void main(String[] args) {
        SpringApplication.run(WriteGameToFile.class ,args);
    }
}
