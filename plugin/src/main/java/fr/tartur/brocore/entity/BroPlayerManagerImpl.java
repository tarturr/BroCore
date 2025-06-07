package fr.tartur.brocore.entity;

import fr.tartur.brocore.SQLiteDataSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class BroPlayerManagerImpl implements BroPlayerManager {
    
    private final Map<UUID, BroPlayer> players;
    private final Logger log;
    private final LuckPerms luckPerms;
    private final SQLiteDataSource source;

    /**
     * Basic class constructor, which needs the {@code SQLiteDataSource} to make database interactions and the plugin
     * {@code Logger} to send messages to the console.
     * 
     * @param log The plugin {@link Logger}.
     * @param source The {@link SQLiteDataSource} needed to make database interactions.
     */
    public BroPlayerManagerImpl(Logger log, SQLiteDataSource source) {
        this.log = log;
        this.players = new HashMap<>();
        
        final RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager()
                .getRegistration(LuckPerms.class);
        
        this.luckPerms = provider == null ? null : provider.getProvider();
        this.source = source;
    }

    @Override
    public Optional<BroPlayer> getPlayer(Player player) {
        return Optional.ofNullable(this.players.get(player.getUniqueId()));
    }

    @Override
    public boolean isLuckPermsLoaded() {
        return this.luckPerms != null;
    }

    @Override
    public Optional<User> getLuckPermsData(Player player) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getLuckPermsData(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public BroPlayer join(Player player) {
        final UUID uuid = player.getUniqueId();
        final BroPlayer broPlayer = new BroPlayerImpl(uuid);
        this.fetch(broPlayer);
        this.players.put(uuid, broPlayer);
        
        return broPlayer;
    }

    @Override
    public BroPlayer leave(Player player) {
        final UUID uuid = player.getUniqueId();
        final BroPlayer broPlayer = this.players.remove(uuid);
        
        if (broPlayer != null) {
            this.save(broPlayer);
        }
        
        return broPlayer;
    }

    @Override
    public void create(BroPlayer player) {
        final Optional<Connection> connectionTrial = this.source.getConnection();
        
        if (connectionTrial.isEmpty()) {
            return;
        }
        
        try (final Connection connection = connectionTrial.get()) {
            final PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO bros(uuid, pseudo, exp, balance) " +
                    "VALUES (?, ?, ?, ?)"
            );
            
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.setDouble(3, player.getExperience());
            statement.setDouble(4, player.getBalance());
            statement.executeUpdate();
        } catch (SQLException exception) {
            log.severe("An error has occurred while inserting data of player '%s' in the database: %s"
                    .formatted(player.getName(), exception));

            player.asBukkit().sendMessage(Component.text("Une erreur est survenue lors de la sauvegarde de " +
                    "vos données. Contactez un administrateur au plus vite.").color(NamedTextColor.RED));
        }
    }

    @Override
    public void save(BroPlayer player) {
        if (!player.isEdited()) {
            return;
        }
        
        final Optional<Connection> connectionTrial = this.source.getConnection();
        
        if (connectionTrial.isEmpty()) {
            return;
        }
        
        try (final Connection connection = connectionTrial.get()) {
            final PreparedStatement statement = connection.prepareStatement("UPDATE bros " +
                    "SET exp = ?, balance = ?" +
                    "WHERE uuid = ?");
            statement.setDouble(1, player.getExperience());
            statement.setDouble(2, player.getBalance());
            statement.setString(3, player.getUniqueId().toString());
            
            statement.executeUpdate();
            player.save();
        } catch (SQLException exception) {
            log.severe("An error has occurred while saving data of player '%s' in the database: %s"
                    .formatted(player.getName(), exception));
            
            player.asBukkit().sendMessage(Component.text("Une erreur est survenue lors de la sauvegarde de " +
                    "vos données. Contactez un administrateur au plus vite.").color(NamedTextColor.RED));
        }
    }

    @Override
    public void saveAll() {
        this.players.values().forEach(this::save);
    }

    @Override
    public void fetch(BroPlayer player) {
        final Optional<Connection> connectionTrial = this.source.getConnection();
        
        if (connectionTrial.isEmpty()) {
            return;
        }
        
        try (final Connection connection = connectionTrial.get()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT exp, balance " +
                    "FROM bros WHERE uuid = ?");
            statement.setString(1, player.getUniqueId().toString());
            
            final ResultSet result = statement.executeQuery();
            
            if (result.next()) {
                player.setExperience(result.getDouble("exp"));
                player.setBalance(result.getDouble("balance"));
            } else {
                this.create(player);
            }
        } catch (SQLException exception) {
            log.severe("An error has occurred while fetching data of player '%s' from the database: %s"
                    .formatted(player.getName(), exception));

            player.asBukkit().sendMessage(Component.text("Une erreur est survenue lors de la récupération de " +
                    "vos données. Contactez un administrateur au plus vite.").color(NamedTextColor.RED));
        }
    }
}
