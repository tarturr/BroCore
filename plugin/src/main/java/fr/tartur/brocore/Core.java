package fr.tartur.brocore;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import fr.tartur.brocore.commands.BroDataCommand;
import fr.tartur.brocore.entity.BroPlayerManager;
import fr.tartur.brocore.entity.BroPlayerManagerImpl;
import fr.tartur.brocore.events.PlayerInOutEvent;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
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
        
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            final BroDataCommand data = new BroDataCommand(this.manager);
            final var field = Commands.argument("field", StringArgumentType.word())
                    .suggests(data::suggestField);
            
            // "/bro <player> set  <field> <amount>"
            // "/bro <player> info <field>"
            commands.registrar().register(Commands.literal("bro")
                    .then(Commands.argument("player", ArgumentTypes.player())
                            .then(Commands.literal("set")
                                    .then(field
                                            .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0d))
                                                    .executes(data::setCommand)
                                            )
                                    )
                            )
                            .then(Commands.literal("info")
                                    .then(field.executes(data::infoCommand)))
                    )
                    .build());
        });
        
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