package fr.tartur.brocore;

import fr.tartur.brocore.commands.BroDataCommand;
import fr.tartur.brocore.entity.BroPlayerManager;
import fr.tartur.brocore.entity.BroPlayerManagerImpl;
import fr.tartur.brocore.events.PlayerInOutEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {
    
    private BroPlayerManager manager;
    private SQLiteDataSource source;

    @Override
    public void onEnable() {
        super.saveDefaultConfig();
        
        this.source = new SQLiteDataSource(this);
        this.source.init(getResource("init-db.sql"));
        
        this.manager = new BroPlayerManagerImpl(getLogger(), this.source);
        
        getServer().getPluginManager().registerEvents(new PlayerInOutEvent(this.manager), this);
        
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
            commands.registrar().register(new BroDataCommand(this.manager).getCommand())
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
    
}