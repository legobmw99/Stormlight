package com.legobmw99.stormlight.network.packets;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.util.Surge;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SurgePacket {
    private final boolean first;
    private final BlockPos looking;
    private final boolean shiftHeld;

    public SurgePacket(boolean first, @Nullable BlockPos looking, boolean shiftHeld) {
        this.first = first;
        this.looking = looking;
        this.shiftHeld = shiftHeld;
    }

    public static SurgePacket decode(PacketBuffer buf) {
        return new SurgePacket(buf.readBoolean(), buf.readBoolean() ? buf.readBlockPos() : null, buf.readBoolean());
    }

    public void encode(PacketBuffer buf) {
        buf.writeBoolean(first);
        boolean hasBlock = looking != null;
        buf.writeBoolean(hasBlock);
        if (hasBlock) {
            buf.writeBlockPos(looking);
        }
        buf.writeBoolean(shiftHeld);
    }


    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                if (player.hasEffect(PowersSetup.STORMLIGHT.get())) {
                    player.getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(data -> {
                        if (data.isKnight()) {
                            Surge surge = this.first ? data.getOrder().getFirst() : data.getOrder().getSecond();
                            surge.fire(player, looking, shiftHeld);
                        }
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
