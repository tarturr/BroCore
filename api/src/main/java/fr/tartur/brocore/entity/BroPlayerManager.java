package fr.tartur.brocore.entity;

import org.bukkit.entity.Player;

import java.util.Optional;

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
     * Adds a player to the {@code BroPlayer} list.
     *
     * @param player The connecting player.
     */
    void join(Player player);

    /**
     * Removes a player from the {@code BroPlayer} list.
     *
     * @param player The disconnecting player.
     */
    void leave(Player player);

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
