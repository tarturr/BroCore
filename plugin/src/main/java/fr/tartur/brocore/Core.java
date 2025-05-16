package fr.tartur.brocore;

import fr.tartur.brocore.entity.BroPlayerManager;
import fr.tartur.brocore.entity.BroPlayerManagerImpl;
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