package fr.tartur.brocore.events;

import fr.tartur.brocore.entity.BroPlayer;
import fr.tartur.brocore.entity.BroPlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerInOutEvent implements Listener {

    private final BroPlayerManager manager;

    public PlayerInOutEvent(BroPlayerManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final BroPlayer player = this.manager.join(event.getPlayer());
        event.joinMessage(this.styleMessage(Component.text('+', NamedTextColor.DARK_GREEN), player.getName()));
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final BroPlayer player = this.manager.leave(event.getPlayer());
        event.quitMessage(this.styleMessage(Component.text('-', NamedTextColor.DARK_RED), player.getName()));
    }
    
    private Component styleMessage(Component icon, String playerName) {
        return Component.text("[", NamedTextColor.GRAY)
                .append(icon)
                .append(Component.text("] ", NamedTextColor.GRAY))
                .append(Component.text(playerName, NamedTextColor.YELLOW));
    }
    
}
