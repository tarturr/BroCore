package fr.tartur.brocore.commands;

import io.papermc.paper.command.brigadier.Commands;

/**
 * Interface used to register commands.
 * 
 * @see RegisterableCommand#register(Commands)
 */
public interface RegisterableCommand {
    
    /**
     * Method called to register one or more command(s).
     * 
     * @param registrar The command registrar.
     */
    void register(Commands registrar);
    
}
