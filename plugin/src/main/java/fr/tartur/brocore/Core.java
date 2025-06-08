package fr.tartur.brocore;

import fr.tartur.brocore.commands.BroDataCommand;
import fr.tartur.brocore.commands.RegisterableCommand;
import fr.tartur.brocore.database.DatabaseDataSource;
import fr.tartur.brocore.database.DatabaseType;
import fr.tartur.brocore.database.MariaDBHikariConfigProvider;
import fr.tartur.brocore.database.SQLiteHikariConfigProvider;
import fr.tartur.brocore.entity.BroPlayerManager;
import fr.tartur.brocore.entity.BroPlayerManagerImpl;
import fr.tartur.brocore.events.PlayerChatListener;
import fr.tartur.brocore.events.PlayerInOutListener;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class Core extends JavaPlugin {
    
    private BroPlayerManager manager;
    private DatabaseDataSource source;

    @Override
    public void onEnable() {
        super.saveDefaultConfig();
        
        this.source = new DatabaseDataSource(this, switch (this.getDatabaseType()) {
            case MARIADB -> new MariaDBHikariConfigProvider(getConfig());
            case SQLITE -> new SQLiteHikariConfigProvider(this);
        });
        this.source.init(getResource("init-db.sql"));
        this.manager = new BroPlayerManagerImpl(getLogger(), this.source);
        
        this.registerCommands(
                new BroDataCommand(this.manager)
        );
        
        this.registerEvents(
                new PlayerInOutListener(this.manager),
                new PlayerChatListener(this.manager)
        );
        
        getLogger().info("The BROS CORE is LOADED!!! GLHF my BABYCHOUUUUS");
    }

    @Override
    public void onDisable() {
        this.manager.saveAll();
        this.source.close();
        
        getLogger().info("No way we're already stopping the game... bye guys, see you soon!! (Really, you will " +
                "come back. That's an order.)");
    }

    public BroPlayerManager getPlayerManager() {
        return this.manager;
    }

    private void registerCommands(RegisterableCommand... commands) {
        for (final RegisterableCommand command : commands) {
            this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, 
                    event -> command.register(event.registrar()));
        }
    }

    private void registerEvents(Listener... listeners) {
        for (final Listener listener : listeners) {
            super.getServer().getPluginManager().registerEvents(listener, this);
        }
    }
    
    private DatabaseType getDatabaseType() {
        final String dbTypeName = super.getConfig().getString("database.type", "UNDEFINED");
        final Optional<DatabaseType> foundDbType = DatabaseType.from(dbTypeName);
        final DatabaseType dbType;

        if (foundDbType.isPresent()) {
            dbType = foundDbType.get();
        } else {
            dbType = DatabaseType.MARIADB;
            
            getLogger().warning(("Database service '%s' found at database.type in config.yml is not supported by " +
                    "the plugin. Defaults to '%s'").formatted(dbTypeName, dbType.name()));
        }
        
        return dbType;
    }
    
}