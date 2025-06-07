package fr.tartur.brocore.entity;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface BroPlayer {

    /**
     * Gets the actual {@code Player} object the class is wrapping.
     *
     * @return The concerned {@link Player}.
     */
    Player asBukkit();

    /**
     * Gets the {@code UUID} of the player.
     *
     * @return the {@link UUID} of the player.
     */
    UUID getUniqueId();

    /**
     * Gets the name of the player.
     *
     * @return The player's name.
     */
    String getName();

    /**
     * Gets the player's experience from cache.
     *
     * @return The player's experience.
     */
    double getExperience();

    /**
     * Sets the amount of experience to the provided level.
     *
     * @param amount The new experience amount.
     */
    void setExperience(double amount);

    /**
     * Adds the provided amount of experience to the player.
     *
     * @param amount The amount of experience to add to the player.
     */
    void addExperience(double amount);

    /**
     * Removes the provided amount of experience to the player.
     *
     * @param amount The amount of experience to remove from the player.
     */
    void removeExperience(double amount);

    /**
     * Gets the player's balance from cache.
     *
     * @return The player's balance.
     */
    double getBalance();

    /**
     * Sets the amount of money to the provided amount.
     *
     * @param amount The new balance amount.
     */
    void setBalance(double amount);

    /**
     * Adds the provided amount of money to the player's balance.
     *
     * @param amount The amount of money to add to the player's balance.
     */
    void addMoney(double amount);

    /**
     * Removes the provided amount of money to the player's balance.
     *
     * @param amount The amount of money to remove from the player's balance.
     */
    void removeMoney(double amount);

    /**
     * Tells whether the player needs its data to be saved in the database.
     *
     * @return {@code true} if the player's data has changed since its last save in database, {@code false} otherwise.
     */
    boolean isEdited();

    /**
     * Puts the player's data in the "saved" state, but actually makes no database interaction.
     *
     * @see BroPlayer#isEdited()
     */
    void save();
    
}
