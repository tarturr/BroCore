package fr.tartur.brocore.database;

import com.zaxxer.hikari.HikariDataSource;
import fr.tartur.brocore.Core;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Class holding the SQLite data source.
 */
public class DatabaseDataSource {
    
    private final HikariDataSource database;
    private final Core core;
    private final Logger log;

    /**
     * Creates a new setup for the SQLite database.
     * 
     * @param core The plugin instance.
     */
    public DatabaseDataSource(Core core, HikariConfigProvider provider) {
        this.core = core;
        this.log = core.getLogger();
        this.database = new HikariDataSource(provider.getConfig());
    }

    /**
     * Initializes the SQLite tables via a file input stream containing the appropriated SQL statement. If an exception
     * was thrown during the process, an ERROR message is send to the console. If the provided stream is {@code null},
     * a WARN message is sent to the console.
     *
     * @param initFileStream The {@code InputStream} containing the appropriated SQL statement.
     */
    public void init(InputStream initFileStream) {
        final Optional<File> initFile = this.getInitFile();
        
        // If a SQL file was found AND the database could be initialized using its contents.
        if (initFile.isPresent() && this.initWithFile(initFile.get())) {
            log.info("The database was successfully initialized with the already existing SQL file.");
            return;
        }
        
        log.info("No existing SQL initialization file could be read. Trying to create one which contains basic SQL " +
                "initialization code...");
        
        if (initFileStream == null) {
            throw new RuntimeException("The SQL initialization file was removed from the plugin resources folder and " +
                    "no existing file could be read at the location provided in config.yml");
        }
        
        final byte[] sql;
        
        try (initFileStream) {
            sql = initFileStream.readAllBytes();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        if (this.createInitFile(sql)) {
            log.info("Successfully created the basic SQL file!");
        } else {
            log.warning("Could not create the basic SQL file.");
        }
        
        log.info("Initializing the SQLite tables with the basic SQL initialization code...");
        
        if (this.initSQLTables(new String(sql))) {
            log.info("Success!");
        }
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
            log.warning("Could not establish a connection to the SQLite database: " + exception);
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

    /**
     * Gets the SQL initialization file if it exists and returns it in an {@code Optional<File>}, or
     * {@code Optional.empty()} if it does not.
     * 
     * @return An {@link Optional<File>} which contains the SQL initialization file if it exists, or
     * {@link Optional#empty()} if it does not.
     */
    private Optional<File> getInitFile() {
        final File init = new File(this.getPath("database.init"));
        return init.exists() ? Optional.of(init) : Optional.empty();
    }

    /**
     * Creates the SQLite initialisation file with the provided data if it does not exist and no exception was thrown
     * during the creation process.
     * 
     * @param code The SQL initialization code.
     * @return {@code true} if the file was successfully created, {@code false} otherwise.
     */
    private boolean createInitFile(byte[] code) {
        final File init = new File(this.getPath("database.init"));
        
        try {
            if (init.createNewFile()) {
                try (final FileOutputStream output = new FileOutputStream(init)) {
                    output.write(code);
                }
            }
        } catch (IOException exception) {
            log.warning("Could not create the SQL initialization file.");
            return false;
        }
        
        return true;
    }

    /**
     * Initializes the SQLite database with the provided SQL file.
     * 
     * @param initFile The file containing the SQL initialization code.
     * @return {@code true} if the initialization was successful, {@code false} if any exception was thrown during the
     * process.
     */
    private boolean initWithFile(File initFile) {
        final String init;
        
        try (final FileInputStream input = new FileInputStream(initFile)) {
            init = new String(input.readAllBytes());
        } catch (IOException exception) {
            log.severe("An error has occurred while reading the new SQL initialization file: " + exception);
            return false;
        }
        
        return this.initSQLTables(init);
    }

    /**
     * Initializes the database using the provided SQL request.
     * 
     * @param request The SQL request which will initialize the database.
     * @return {@code true} if the database was successfully initialized, {@code false} otherwise.
     */
    private boolean initSQLTables(String request) {
        final Optional<Connection> connectionTrial = this.getConnection();
        
        if (connectionTrial.isEmpty()) {
            return false;
        }
        
        try (final Statement statement = connectionTrial.get().createStatement()) {
            statement.executeUpdate(request);
        } catch (SQLException exception) {
            log.severe("An error has occurred while initializing the SQLite tables: " + exception);
            return false;
        }
        
        return true;
    }

    /**
     * Returns the path (relative to the server folder) to the file specified by the provided configuration section
     * (only relative to the plugin data folder).
     * 
     * @param confPath The configuration path indicating the file path.
     * @return The file path (relative to the server folder).
     */
    private String getPath(String confPath) {
        return this.core.getDataPath() + File.separator + this.core.getConfig().getString(confPath);
    }
    
}
