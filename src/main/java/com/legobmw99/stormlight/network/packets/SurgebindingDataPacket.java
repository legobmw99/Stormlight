package com.legobmw99.stormlight.network.packets;


import com.legobmw99.stormlight.api.ISurgebindingData;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SurgebindingDataPacket {

    private final CompoundNBT nbt;
    private final UUID uuid;


    public SurgebindingDataPacket(ISurgebindingData data, PlayerEntity player) {
        this.uuid = player.getUUID();
        this.nbt = (data != null && SurgebindingCapability.PLAYER_CAP != null) ? (CompoundNBT) SurgebindingCapability.PLAYER_CAP.writeNBT(data, null) : new CompoundNBT();

    }

    private SurgebindingDataPacket(CompoundNBT data, UUID uuid) {
        this.nbt = data;
        this.uuid = uuid;
    }

    public static SurgebindingDataPacket decode(PacketBuffer buf) {
        return new SurgebindingDataPacket(buf.readNbt(), buf.readUUID());
    }

    public void encode(PacketBuffer buf) {
        buf.writeNbt(this.nbt);
        buf.writeUUID(this.uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = Minecraft.getInstance().level.getPlayerByUUID(this.uuid);
            if (player != null && SurgebindingCapability.PLAYER_CAP != null) {
                player.getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(cap -> SurgebindingCapability.PLAYER_CAP.readNBT(cap, null, this.nbt));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}