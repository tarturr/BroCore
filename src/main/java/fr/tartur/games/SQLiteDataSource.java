package fr.tartur.games;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Class holding the SQLite data source.
 */
public class SQLiteDataSource {

    private static final Logger log = LogManager.getLogger(SQLiteDataSource.class);
    
    private final HikariDataSource database;

    /**
     * Creates a new setup for the SQLite database.
     * 
     * @param pluginConfig The {@code config.yml} plugin file.
     */
    public SQLiteDataSource(FileConfiguration pluginConfig) {
        final HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.sqlite.SQLiteDataSource");
        config.setJdbcUrl("jdbc:sqlite:" + pluginConfig.get("path_to_db"));
        this.database = new HikariDataSource(config);
    }

    /**
     * Initializes the SQLite tables via a file input stream containing the appropriated SQL statement. If an exception
     * was thrown during the process, an ERROR message is send to the console. If the provided stream is {@code null},
     * a WARN message is sent to the console.
     *
     * @param initFileStream The {@code InputStream} containing the appropriated SQL statement.
     */
    public void init(InputStream initFileStream) {
        if (initFileStream == null) {
            log.warn("Could not find the file containing the statements initializing the SQLite tables.");
            return;
        }
        
        try (initFileStream; final Statement statement = this.database.getConnection().createStatement()) {
            final String init = new String(initFileStream.readAllBytes());
            log.info("Initializing tables with statement:\n{}", init);
            statement.executeUpdate(init);
        } catch (IOException exception) {
            log.error("An error has occurred while reading the SQLite init program", exception);
            return;
        } catch (SQLException exception) {
            log.error("An error has occurred while initializing the SQLite tables", exception);
            return;
        }
        
        log.info("Successfully initialized the SQLite database.");
    }

    /**
     * Returns an {@code Optional<Connection>} containing the Connection object if the plugin successfully established
     * a connection to the SQLite database, or {@link Optional#empty()} otherwise. If an {@link SQLException} was
     * thrown, the plugin won't stop and send a WARN message to the console.
     * 
     * @return The {@code Optional<Connection>} which might contain a database {@link Connection} object.
     */
    public Optional<Connection> getConnection() {
        try {
            return Optional.of(this.database.getConnection());
        } catch (SQLException exception) {
            log.warn("Could not establish a connection to the SQLite database", exception);
            return Optional.empty();
        }
    }

    /**
     * Closes any connection established to the SQLite database.
     */
    public void close() {
        this.database.close();
        log.info("SQLite database connection was closed.");
    }
    
}
