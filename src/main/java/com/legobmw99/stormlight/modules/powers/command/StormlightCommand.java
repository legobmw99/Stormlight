package com.legobmw99.stormlight.modules.powers.command;

import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.network.Network;
import com.legobmw99.stormlight.util.Ideal;
import com.legobmw99.stormlight.util.Order;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

public class StormlightCommand {


    private static Predicate<CommandSource> permissions(int level) {
        return (player) -> player.hasPermission(level);
    }

    private static Collection<ServerPlayerEntity> sender(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        return Collections.singleton(ctx.getSource().getPlayerOrException());
    }

    private static Collection<ServerPlayerEntity> targets(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        return EntityArgument.getPlayers(ctx, "targets");
    }


    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> root = Commands.literal("stormlight").requires(permissions(0));

        LiteralArgumentBuilder<CommandSource> order = Commands.literal("order")

                                                              .then(Commands
                                                                            .literal("get")
                                                                            .requires(permissions(0))
                                                                            .executes(ctx -> getOrder(ctx, sender(ctx)))
                                                                            .then(Commands
                                                                                          .argument("targets", EntityArgument.players())
                                                                                          .executes(ctx -> getOrder(ctx, targets(ctx)))))


                                                              .then(Commands
                                                                            .literal("set")
                                                                            .requires(permissions(2))
                                                                            .then(Commands
                                                                                          .argument("order", StormlightArgType.OrderType.INSTANCE)
                                                                                          .executes(ctx -> setOrder(ctx, sender(ctx)))
                                                                                          .then(Commands
                                                                                                        .argument("targets", EntityArgument.players())
                                                                                                        .executes(ctx -> setOrder(ctx, targets(ctx))))));

        LiteralArgumentBuilder<CommandSource> ideal = Commands.literal("ideal")

                                                              .then(Commands
                                                                            .literal("get")
                                                                            .requires(permissions(0))
                                                                            .executes(ctx -> getIdeal(ctx, sender(ctx)))
                                                                            .then(Commands
                                                                                          .argument("targets", EntityArgument.players())
                                                                                          .executes(ctx -> getIdeal(ctx, targets(ctx)))))


                                                              .then(Commands
                                                                            .literal("set")
                                                                            .requires(permissions(2))
                                                                            .then(Commands
                                                                                          .argument("ideal", StormlightArgType.IdealType.INSTANCE)
                                                                                          .executes(ctx -> setIdeal(ctx, sender(ctx)))
                                                                                          .then(Commands
                                                                                                        .argument("targets", EntityArgument.players())
                                                                                                        .executes(ctx -> setIdeal(ctx, targets(ctx))))));


        root.then(order);
        root.then(ideal);

        dispatcher.register(root);
    }


    private static int setOrder(CommandContext<CommandSource> ctx, Collection<ServerPlayerEntity> players) {
        int i = 0;
        Order order = ctx.getArgument("order", Order.class);
        for (ServerPlayerEntity player : players) {
            int success = player.getCapability(SurgebindingCapability.PLAYER_CAP).map(data -> {
                data.setOrder(order);
                ctx.getSource().sendSuccess(new TranslationTextComponent("commands.stormlight.setorder", player.getDisplayName(), order.toString()), true);
                return 1;
            }).orElse(0);
            if (success == 1) {
                Network.sync(player);
            }
            i += success;
        }
        return i;
    }


    private static int setIdeal(CommandContext<CommandSource> ctx, Collection<ServerPlayerEntity> players) {
        int i = 0;
        Ideal ideal = ctx.getArgument("ideal", Ideal.class);
        for (ServerPlayerEntity player : players) {
            int success = player.getCapability(SurgebindingCapability.PLAYER_CAP).map(data -> {
                data.setIdeal(ideal);
                ctx.getSource().sendSuccess(new TranslationTextComponent("commands.stormlight.setideal", player.getDisplayName(), ideal.toString()), true);
                return 1;
            }).orElse(0);
            if (success == 1) {
                Network.sync(player);
            }
            i += success;
        }
        return i;
    }

    private static int getOrder(CommandContext<CommandSource> ctx, Collection<ServerPlayerEntity> players) {
        int i = 0;
        for (ServerPlayerEntity player : players) {
            i += player.getCapability(SurgebindingCapability.PLAYER_CAP).map(data -> {
                ctx.getSource().sendSuccess(new TranslationTextComponent("commands.stormlight.getorder", player.getDisplayName(), data.getOrder().toString()), true);
                return 1;
            }).orElse(0);
        }
        return i;
    }

    private static int getIdeal(CommandContext<CommandSource> ctx, Collection<ServerPlayerEntity> players) {
        int i = 0;
        for (ServerPlayerEntity player : players) {
            i += player.getCapability(SurgebindingCapability.PLAYER_CAP).map(data -> {
                ctx.getSource().sendSuccess(new TranslationTextComponent("commands.stormlight.getideal", player.getDisplayName(), data.getIdeal().toString()), true);
                return 1;
            }).orElse(0);
        }
        return i;
    }
}
