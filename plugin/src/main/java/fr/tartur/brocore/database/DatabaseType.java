package fr.tartur.brocore.database;

import java.util.Optional;

/**
 * Enumeration which groups any database service supported by the plugin.
 */
public enum DatabaseType {
    
    MARIADB,
    SQLITE;

    /**
     * Gets the {@code DatabaseType} appropriated to the given name, ignoring case.
     * 
     * @param name The database service name.
     * @return An {@code Optional} if the type was correctly spelled and is supported by the plugin, or {@code false}
     * otherwise.
     */
    public static Optional<DatabaseType> from(String name) {
        for (final DatabaseType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return Optional.of(type);
            }
        }
        
        return Optional.empty();
    }
    
}
