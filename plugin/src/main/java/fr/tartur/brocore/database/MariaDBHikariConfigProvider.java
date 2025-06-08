package fr.tartur.brocore.database;

import com.zaxxer.hikari.HikariConfig;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Class used to initialize a {@link HikariConfig} instance for the MariaDB database service.
 */
public class MariaDBHikariConfigProvider implements HikariConfigProvider {
    
    private final FileConfiguration configuration;

    /**
     * Class constructor, which needs the plugin's {@link FileConfiguration} instance to read database credentials.
     * 
     * @param configuration The plugin's {@code config.yml} file.
     */
    public MariaDBHikariConfigProvider(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Gets the {@link HikariConfig} instance.
     *
     * @return The said instance.
     */
    @Override
    public HikariConfig getConfig() {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mariadb://%s/%s".formatted(
                this.configuration.getString("database.credentials.host"),
                this.configuration.getString("database.credentials.database")
        ));
        config.setUsername(this.configuration.getString("database.credentials.user"));
        config.setPassword(this.configuration.getString("database.credentials.password"));
        return config;
    }
    
}
