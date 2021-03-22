package com.legobmw99.stormlight.network.packets;


import com.legobmw99.stormlight.modules.powers.StormlightCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class StormlightCapabilityPacket {

    private final CompoundNBT nbt;
    private final UUID player;


    public StormlightCapabilityPacket(StormlightCapability data, UUID player) {
        this(data != null ? data.serializeNBT() : new StormlightCapability().serializeNBT(), player);
    }

    private StormlightCapabilityPacket(CompoundNBT data, UUID player) {
        this.nbt = data;
        this.player = player;
    }

    public static StormlightCapabilityPacket decode(PacketBuffer buf) {
        return new StormlightCapabilityPacket(buf.readNbt(), buf.readUUID());
    }

    public void encode(PacketBuffer buf) {
        buf.writeNbt(this.nbt);
        buf.writeUUID(this.player);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = Minecraft.getInstance().level.getPlayerByUUID(this.player);
            if (player != null) {
                StormlightCapability playerCap = StormlightCapability.forPlayer(player);
                playerCap.deserializeNBT(this.nbt);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}