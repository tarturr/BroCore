package fr.tartur.brocore.events;

import fr.tartur.brocore.entity.BroPlayerManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.node.NodeType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener {
    
    private final BroPlayerManager manager;

    public PlayerChatListener(BroPlayerManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        event.message(this.getDisplayName(event.getPlayer())
                .append(Component.text(" : ", NamedTextColor.GRAY))
                .append(event.message()));
    }
    
    private Component getDisplayName(Player player) {
        final var user = this.manager.getLuckPermsData(player);
        final Component defaultDisplay = Component.text(player.getName(), NamedTextColor.GRAY);

        return user.map(value -> value.getNodes(NodeType.PREFIX).stream().findFirst()
                .map(prefix -> 
                        MiniMessage.miniMessage().deserialize(prefix.getMetaValue())
                                .append(Component.text(" " + player.getName()))
                ).orElse(
                        defaultDisplay
                )
        ).orElse(defaultDisplay);
    }
    
}
