package fr.tartur.brocore.database;

import com.zaxxer.hikari.HikariConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Class used to initialize a {@link HikariConfig} instance for the SQLite database service.
 */
public class SQLiteHikariConfigProvider implements HikariConfigProvider {
    
    private final String databasePath;

    /**
     * Class constructor, which needs a {@link JavaPlugin} instance to get the plugin data path and its
     * {@code config.yml}.
     *
     * @param plugin The main plugin instance.
     */
    public SQLiteHikariConfigProvider(JavaPlugin plugin) {
        this.databasePath = plugin.getDataPath() + File.separator + plugin.getConfig().getString("database.file");
    }

    /**
     * Gets the {@link HikariConfig} instance.
     *
     * @return The said instance.
     */
    @Override
    public HikariConfig getConfig() {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + this.databasePath);
        return config;
    }
    
}
