package app.PersistenceManagers;

import app.ReadWriteFile.File;
import app.StatsEngine.TotalGamesStatsCalculator;

/**
 * Created by askowronski on 7/17/17.
 */
public class StatsPersistenceManager {

    public static String FILE_PATH = "pingpongStats.txt";

    private final File file;

    public StatsPersistenceManager(String filePath) {
        this.file = new File(filePath);
    }

    public StatsPersistenceManager() {
        this.file = new File(FILE_PATH);
    }




}
