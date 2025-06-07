package fr.tartur.brocore.database;

import com.zaxxer.hikari.HikariConfig;

/**
 * Interface used to initialize a {@link HikariConfig} instance.
 */
public interface HikariConfigProvider {

    /**
     * Gets the {@link HikariConfig} instance.
     * 
     * @return The said instance.
     */
    HikariConfig getConfig();
    
}
