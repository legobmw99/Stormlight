package com.legobmw99.stormlight.modules.powers.command;

import com.legobmw99.stormlight.util.Ideal;
import com.legobmw99.stormlight.util.Order;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class StormlightArgType {

    public static class OrderType implements ArgumentType<Order> {

        public static final OrderType INSTANCE = new OrderType();
        private static final Set<String> types = Arrays.stream(Order.values()).map(Order::getName).collect(Collectors.toSet());
        private static final DynamicCommandExceptionType unknown_power = new DynamicCommandExceptionType(
                o -> new TranslatableComponent("commands.stormlight.unrecognized_order", o));

        @Override
        public Order parse(StringReader reader) throws CommandSyntaxException {
            String in = reader.readUnquotedString();
            if (types.contains(in)) {
                return Order.valueOf(in.toUpperCase(Locale.ROOT));
            }
            throw unknown_power.create(in);
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return SharedSuggestionProvider.suggest(types, builder).toCompletableFuture();
        }

        @Override
        public Collection<String> getExamples() {
            return types;
        }
    }

    public static class IdealType implements ArgumentType<Ideal> {

        public static final IdealType INSTANCE = new IdealType();
        private static final Set<String> types = Arrays.stream(Ideal.values()).map(Ideal::getName).collect(Collectors.toSet());
        private static final DynamicCommandExceptionType unknown_power = new DynamicCommandExceptionType(
                o -> new TranslatableComponent("commands.stormlight.unrecognized_ideal", o));


        @Override
        public Ideal parse(StringReader reader) throws CommandSyntaxException {
            String in = reader.readUnquotedString();
            if (types.contains(in)) {
                return Ideal.valueOf(in.toUpperCase(Locale.ROOT));
            }
            throw unknown_power.create(in);
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return SharedSuggestionProvider.suggest(types, builder).toCompletableFuture();
        }

        @Override
        public Collection<String> getExamples() {
            return types;
        }
    }
}
