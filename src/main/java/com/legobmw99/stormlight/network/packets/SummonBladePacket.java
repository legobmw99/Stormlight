package com.legobmw99.stormlight.network.packets;

import com.legobmw99.stormlight.modules.combat.item.ShardbladeItem;
import com.legobmw99.stormlight.modules.powers.StormlightCapability;
import com.legobmw99.stormlight.network.Network;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SummonBladePacket {

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                StormlightCapability cap = StormlightCapability.forPlayer(player);
                if (cap.isKnight() && cap.isBladeStored()) {
                    cap.addBladeToInventory(player);
                    // TODO when spren, hide them
                } else {
                    if (player.getMainHandItem().getItem() instanceof ShardbladeItem && ((ShardbladeItem) player.getMainHandItem().getItem()).getOrder() == cap.getOrder()) {
                        cap.storeBlade(player.getMainHandItem());
                        player.inventory.setItem(player.inventory.selected, ItemStack.EMPTY);
                    }
                }
                Network.sync(cap, player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}