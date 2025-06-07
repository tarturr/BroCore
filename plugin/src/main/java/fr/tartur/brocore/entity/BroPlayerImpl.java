package fr.tartur.brocore.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Represents a {@link Player} class wrapper, adding new functionalities related to Bro players' data.
 */
public class BroPlayerImpl implements BroPlayer {
    
    private final UUID uuid;
    private final Player player;
    private double experience;
    private double balance;
    private boolean edited;

    /**
     * Builds a new {@code BroPlayer} with default values in its fields. Consider the use of
     * {@link BroPlayerManagerImpl#fetch(BroPlayer)} if the player needs its data to be fetched from the database.
     * 
     * @param uuid The player's {@link UUID}.
     */
    public BroPlayerImpl(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.experience = 0;
        this.balance = 0;
        this.edited = false;
    }
    
    @Override
    public Player asBukkit() {
        return this.player;
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public double getExperience() {
        return this.experience;
    }

    @Override
    public void setExperience(double amount) {
        this.experience = amount;
        this.edited = true;
    }

    @Override
    public void addExperience(double amount) {
        this.setExperience(this.getExperience() + amount);
    }

    @Override
    public void removeExperience(double amount) {
        this.setExperience(this.getExperience() - amount);
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public void setBalance(double amount) {
        this.balance = amount;
        this.edited = true;
    }

    @Override
    public void addMoney(double amount) {
        this.setBalance(this.getBalance() + amount);
    }

    @Override
    public void removeMoney(double amount) {
        this.setBalance(this.getBalance() - amount);
    }

    @Override
    public boolean isEdited() {
        return this.edited;
    }

    @Override
    public void save() {
        this.edited = false;
    }
    
}
