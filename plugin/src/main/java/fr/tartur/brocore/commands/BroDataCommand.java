package fr.tartur.brocore.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.tartur.brocore.entity.BroPlayer;
import fr.tartur.brocore.entity.BroPlayerManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BroDataCommand {
    
    private static final List<String> FIELDS = List.of("experience", "balance");
    
    private final BroPlayerManager manager;
    private final LiteralCommandNode<CommandSourceStack> command;

    // "/bro <player> set  <field> <amount>"
    // "/bro <player> info <field>"
    public BroDataCommand(BroPlayerManager manager) {
        this.manager = manager;

        final var field = Commands.argument("field", StringArgumentType.word())
                .suggests(this::suggestField);
        this.command = Commands.literal("bro")
                .then(Commands.argument("player", ArgumentTypes.player())
                        .then(Commands.literal("set")
                                .then(field
                                        .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0d))
                                                .executes(this::setCommand)
                                        )
                                )
                        )
                        .then(Commands.literal("info")
                                .then(field.executes(this::infoCommand)))
                )
                .build();
    }

    public CompletableFuture<Suggestions> suggestField(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        FIELDS.stream()
                .filter(field -> field.startsWith(builder.getRemainingLowerCase()))
                .forEach(builder::suggest);
        
        return builder.buildFuture();
    }

    public int setCommand(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final BroPlayer target = this.getPlayer(ctx);
        final CommandSender sender = ctx.getSource().getSender();
        final String field = ctx.getArgument("field", String.class).toLowerCase();
        final double amount = ctx.getArgument("amount", double.class);
        
        if (field.equals(FIELDS.getFirst())) {
            final Component exp = Component.text(amount + "XP", NamedTextColor.AQUA);
            
            target.addExperience(amount);
            target.asBukkit().sendMessage(exp.append(Component.text(" ont été ajoutés à votre compte !", NamedTextColor.GREEN)));
            sender.sendMessage(exp.append(Component.text(" ont bien été envoyés à %s !".formatted(target.getName()), NamedTextColor.GREEN)));
        } else if (field.equals(FIELDS.get(1))) {
            final Component money = Component.text(amount + "BC", NamedTextColor.YELLOW);

            target.addMoney(amount);
            target.asBukkit().sendMessage(money.append(Component.text(" ont été ajoutés à votre compte !", NamedTextColor.GREEN)));
            sender.sendMessage(money.append(Component.text(" ont bien été envoyés à %s !".formatted(target.getName()), NamedTextColor.GREEN)));
        } else {
            sender.sendMessage(Component.text("Champ inconnu.", NamedTextColor.RED));
            return 0;
        }
        
        return Command.SINGLE_SUCCESS;
    }

    public int infoCommand(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final BroPlayer target = this.getPlayer(ctx);
        final CommandSender sender = ctx.getSource().getSender();
        final String field = ctx.getArgument("field", String.class).toLowerCase();
        
        if (FIELDS.getFirst().equals(field)) {
            sender.sendMessage(Component.text("%s a ".formatted(target.getName()), NamedTextColor.GREEN)
                    .append(Component.text(target.getExperience() + "XP", NamedTextColor.AQUA)));
        } else if (FIELDS.get(1).equals(field)) {
            sender.sendMessage(Component.text("%s a ".formatted(target.getName()), NamedTextColor.GREEN)
                    .append(Component.text(target.getBalance() + "BC", NamedTextColor.YELLOW)));
        } else {
            sender.sendMessage(Component.text("Champ inconnu.", NamedTextColor.RED));
            return 0;
        }
        
        return Command.SINGLE_SUCCESS;
    }
    
    private BroPlayer getPlayer(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final var selector = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
        final Player playerTarget = selector.resolve(ctx.getSource()).getFirst();
        return this.manager.getPlayer(playerTarget).orElseThrow();
    }

    public LiteralCommandNode<CommandSourceStack> getCommand() {
        return command;
    }
}
