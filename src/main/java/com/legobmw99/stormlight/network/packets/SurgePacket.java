package com.legobmw99.stormlight.network.packets;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.util.Surge;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SurgePacket {
    private final Surge surge;
    private final BlockPos looking;
    private final boolean shiftHeld;

    public SurgePacket(Surge surge, @Nullable BlockPos looking, boolean shiftHeld) {
        this.surge = surge;
        this.looking = looking;
        this.shiftHeld = shiftHeld;
    }

    public static SurgePacket decode(PacketBuffer buf) {
        return new SurgePacket(buf.readEnum(Surge.class), buf.readBoolean() ? buf.readBlockPos() : null, buf.readBoolean());
    }

    public void encode(PacketBuffer buf) {
        buf.writeEnum(surge);
        boolean hasBlock = looking != null;
        buf.writeBoolean(hasBlock);
        if (hasBlock) {
            buf.writeBlockPos(looking);
        }
        buf.writeBoolean(shiftHeld);
    }


    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ctx.get().enqueueWork(() -> {
                if (looking != null) {
                    surge.displayEffect(looking, shiftHeld);
                }
            });
            ctx.get().setPacketHandled(true);
        } else if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player != null) {
                    if (player.getCapability(SurgebindingCapability.PLAYER_CAP).filter(data -> data.isKnight() && data.getOrder().hasSurge(surge)).isPresent() &&
                        player.hasEffect(PowersSetup.STORMLIGHT.get())) {
                        surge.fire(player, looking, shiftHeld);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
