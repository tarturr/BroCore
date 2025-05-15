package fr.tartur.games.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BroPlayer {
    
    private final Player player;
    private double experience;
    private double balance;
    private boolean edited;
    
    public BroPlayer(UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
        this.experience = 0;
        this.balance = 0;
        this.edited = false;
    }
    
    public Player asPaper() {
        return this.player;
    }

    public double getExperience() {
        return experience;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setExperience(double experience) {
        this.experience = experience;
        this.edited = true;
    }

    public void setBalance(double balance) {
        this.balance = balance;
        this.edited = true;
    }
    
    public void save() {
        this.edited = false;
    }
    
}
