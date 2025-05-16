package com.github.tarturr.brocore.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a {@link Player} class wrapper, adding new functionalities related to Bro players' data.
 */
public class BroPlayer {
    
    private final UUID uuid;
    private final Player player;
    private List<String> ranks;
    private double experience;
    private double balance;
    private boolean edited;

    /**
     * Builds a new {@code BroPlayer} with default values in its fields. Consider the use of
     * {@link BroPlayerManager#fetch(BroPlayer)} if the player needs its data to be fetched from the database.
     * 
     * @param uuid The player's {@link UUID}.
     */
    public BroPlayer(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.ranks = new ArrayList<>();
        this.experience = 0;
        this.balance = 0;
        this.edited = false;
    }
    
    /**
     * Gets the actual {@code Player} object the class is wrapping.
     *
     * @return The concerned {@link Player}.
     */
    public Player asBukkit() {
        return this.player;
    }

    /**
     * Gets the {@code UUID} of the player.
     *
     * @return the {@link UUID} of the player.
     */
    public UUID getUniqueId() {
        return this.uuid;
    }

    /**
     * Gets the name of the player.
     *
     * @return The player's name.
     */
    public String getName() {
        return "";
    }

    /**
     * Gets the rank list of the player.
     *
     * @return A {@link List} containing all the ranks of the player.
     */
    public List<String> getRanks() {
        return List.of();
    }

    /**
     * Overwrites the player's rank list with the provided one.
     *
     * @param ranks The new player's ranks.
     */
    public void setRanks(List<String> ranks) {
        this.ranks = ranks;
    }

    /**
     * Adds a new rank to the player's rank list.
     *
     * @param rank The rank to add.
     */
    public void addRank(String rank) {
        this.ranks.add(rank);
    }

    /**
     * Removes a rank from the player's rank list.
     *
     * @param rank The rank to remove.
     */
    public void removeRank(String rank) {
        this.ranks.remove(rank);
    }

    /**
     * Gets the player's experience from cache.
     *
     * @return The player's experience.
     */
    public double getExperience() {
        return this.experience;
    }

    /**
     * Sets the amount of experience to the provided level.
     *
     * @param amount The new experience amount.
     */
    public void setExperience(double amount) {
        this.experience = amount;
        this.edited = true;
    }

    /**
     * Adds the provided amount of experience to the player.
     *
     * @param amount The amount of experience to add to the player.
     */
    public void addExperience(double amount) {
        this.setExperience(this.getExperience() + amount);
    }

    /**
     * Removes the provided amount of experience to the player.
     *
     * @param amount The amount of experience to remove from the player.
     */
    public void removeExperience(double amount) {
        this.setExperience(this.getExperience() - amount);
    }

    /**
     * Gets the player's balance from cache.
     *
     * @return The player's balance.
     */
    public double getBalance() {
        return this.balance;
    }

    /**
     * Sets the amount of money to the provided amount.
     *
     * @param amount The new balance amount.
     */
    public void setBalance(double amount) {
        this.balance = amount;
        this.edited = true;
    }

    /**
     * Adds the provided amount of money to the player's balance.
     *
     * @param amount The amount of money to add to the player's balance.
     */
    public void addMoney(double amount) {
        this.setBalance(this.getBalance() + amount);
    }

    /**
     * Removes the provided amount of money to the player's balance.
     *
     * @param amount The amount of money to remove from the player's balance.
     */
    public void removeMoney(double amount) {
        this.setBalance(this.getBalance() - amount);
    }

    /**
     * Tells whether the player needs its data to be saved in the database.
     *
     * @return {@code true} if the player's data has changed since its last save in database, {@code false} otherwise.
     */
    public boolean isEdited() {
        return this.edited;
    }

    /**
     * Puts the player's data in the "saved" state, but actually makes no database interaction.
     *
     * @see BroPlayer#isEdited()
     */
    public void save() {
        this.edited = false;
    }
    
}
