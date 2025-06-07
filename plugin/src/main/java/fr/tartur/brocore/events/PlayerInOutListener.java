package fr.tartur.brocore.events;

import fr.tartur.brocore.entity.BroPlayer;
import fr.tartur.brocore.entity.BroPlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerInOutListener implements Listener {

    private final BroPlayerManager manager;

    public PlayerInOutListener(BroPlayerManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player spigotPlayer = event.getPlayer();
        final BroPlayer player = this.manager.join(spigotPlayer);
        final var user = this.manager.getLuckPermsData(spigotPlayer);
        
        event.joinMessage(user
                .map(value ->
                        this.styleMessage(Component.text('+', NamedTextColor.DARK_GREEN), value, player.getName())
                ).orElseGet(() ->
                        this.styleMessage(Component.text('+', NamedTextColor.DARK_GREEN), player.getName())
                )
        );
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player spigotPlayer = event.getPlayer();
        final BroPlayer player = this.manager.join(spigotPlayer);
        final var user = this.manager.getLuckPermsData(spigotPlayer);

        event.quitMessage(user
                .map(value ->
                        this.styleMessage(Component.text('-', NamedTextColor.DARK_RED), value, player.getName())
                ).orElseGet(() ->
                        this.styleMessage(Component.text('-', NamedTextColor.DARK_RED), player.getName())
                )
        );
    }

    private Component styleMessage(Component icon, User user, String playerName) {
        Component message = Component.text("[", NamedTextColor.GRAY)
                .append(icon)
                .append(Component.text("] ", NamedTextColor.GRAY));

        if (user != null) {
            final var prefix = user.getNodes(NodeType.PREFIX).stream().findFirst();
            
            if (prefix.isPresent()) {
                message = message.append(Component.text(prefix.get().getMetaValue() + " "));
            }
        }

        return message.append(Component.text(playerName, NamedTextColor.YELLOW));
    }

    private Component styleMessage(Component icon, String playerName) {
        return this.styleMessage(icon, null, playerName);
    }
    
}
