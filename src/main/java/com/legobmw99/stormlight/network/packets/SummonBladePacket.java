package com.legobmw99.stormlight.network.packets;

import com.legobmw99.stormlight.modules.combat.item.ShardbladeItem;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.network.Network;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class SummonBladePacket {

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(data -> {
                    if (data.isBladeStored()) {
                        if (data.earnedBlade()) {
                            data.addBladeToInventory(player);
                            // TODO when spren, hide them
                        }
                    } else if (player.getMainHandItem().getItem() instanceof ShardbladeItem &&
                               ((ShardbladeItem) player.getMainHandItem().getItem()).getOrder() == data.getOrder()) {
                        data.storeBlade(player.getMainHandItem());
                        player.getInventory().setItem(player.getInventory().selected, ItemStack.EMPTY);
                    }
                    Network.sync(data, player);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}