package fr.tartur.brocore.entity;

import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

/**
 * Handles the data of a {@link BroPlayer} list, including caching and database interactions.
 */
public interface BroPlayerManager {

    /**
     * Might get the {@code BroPlayer} associated to this player if he is connected.
     *
     * @param player The player to get the {@link BroPlayer} object from.
     * @return An {@link Optional} containing the {@link BroPlayer} instance, or {@link Optional#empty()} if he was not
     * found.
     */
    Optional<BroPlayer> getPlayer(Player player);

    /**
     * Checks if the {@code LuckPerms} dependency is loaded.
     * 
     * @return {@code true} if so, {@code false} otherwise.
     */
    boolean isLuckPermsLoaded();

    /**
     * If the provided {@link Player} has LuckPerms data stored in cache (which is not sure if player is offline),
     * returns a {@link User} object.
     *
     * @param player The player to get the LuckPerms information from.
     * @return An {@code Optional} wrapping the LuckPerms user, or {@code Optional.empty()} if no data was found in
     * cache.
     */
    Optional<User> getLuckPermsData(Player player);

    /**
     * If the {@link Player} associated with the provided {@link UUID} has LuckPerms data stored in cache (which is not
     * sure if player is offline), returns a {@link User} object.
     *
     * @param uuid The UUID of the player to get the LuckPerms information from.
     * @return An {@code Optional} wrapping the LuckPerms user, or {@code Optional.empty()} if no data was found in
     * cache.
     */
    Optional<User> getLuckPermsData(UUID uuid);

    /**
     * Adds a player to the {@code BroPlayer} list.
     *
     * @param player The connecting player.
     * @return The player wrapped in a {@link BroPlayer} instance.
     */
    BroPlayer join(Player player);

    /**
     * Removes a player from the {@code BroPlayer} list.
     *
     * @param player The disconnecting player.
     * @return The player wrapped in a {@link BroPlayer} instance.
     */
    BroPlayer leave(Player player);

    /**
     * Inserts, for the first time, the player's data in the database.
     * 
     * @param player The new player.
     */
    void create(BroPlayer player);

    /**
     * Saves the data of the provided player in the database.
     *
     * @param player The player which needs its data to be saved.
     */
    void save(BroPlayer player);

    /**
     * Saves the data from all the connected players.
     */
    void saveAll();

    /**
     * Fetches the provided player's data from the database.
     *
     * @param player The player which needs its data to be fetched.
     */
    void fetch(BroPlayer player);
    
}
