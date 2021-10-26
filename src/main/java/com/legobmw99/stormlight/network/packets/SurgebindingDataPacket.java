package com.legobmw99.stormlight.network.packets;


import com.legobmw99.stormlight.api.ISurgebindingData;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SurgebindingDataPacket {

    private final CompoundTag nbt;
    private final UUID uuid;


    public SurgebindingDataPacket(ISurgebindingData data, Player player) {
        this.uuid = player.getUUID();
        this.nbt = (data != null) ? data.save() : new CompoundTag();

    }

    private SurgebindingDataPacket(CompoundTag data, UUID uuid) {
        this.nbt = data;
        this.uuid = uuid;
    }

    public static SurgebindingDataPacket decode(FriendlyByteBuf buf) {
        return new SurgebindingDataPacket(buf.readNbt(), buf.readUUID());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.nbt);
        buf.writeUUID(this.uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(this.uuid);
            if (player != null && SurgebindingCapability.PLAYER_CAP != null) {
                player.getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(cap -> cap.load(this.nbt));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}