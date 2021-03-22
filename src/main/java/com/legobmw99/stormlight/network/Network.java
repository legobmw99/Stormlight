package com.legobmw99.stormlight.network;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.powers.StormlightCapability;
import com.legobmw99.stormlight.network.packets.StormlightCapabilityPacket;
import com.legobmw99.stormlight.network.packets.SummonBladePacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Network {

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Stormlight.MODID, "networking"), () -> "1.0", s -> true, s -> true);

    private static int index = 0;

    private static int nextIndex() {
        return index++;
    }

    public static void registerPackets() {

        INSTANCE.registerMessage(nextIndex(), StormlightCapabilityPacket.class, StormlightCapabilityPacket::encode, StormlightCapabilityPacket::decode,
                                 StormlightCapabilityPacket::handle);
        INSTANCE.registerMessage(nextIndex(), SummonBladePacket.class, (self, buf) -> {}, buf -> new SummonBladePacket(), SummonBladePacket::handle);

    }

    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }

    public static void sendTo(Object msg, ServerPlayerEntity player) {
        if (!(player instanceof FakePlayer)) {
            INSTANCE.sendTo(msg, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static void sendTo(Object msg, PacketDistributor.PacketTarget target) {
        INSTANCE.send(target, msg);
    }

    public static void sync(PlayerEntity player) {
        StormlightCapability cap = StormlightCapability.forPlayer(player);
        sync(cap, player);
    }

    public static void sync(StormlightCapability cap, PlayerEntity player) {
        sync(new StormlightCapabilityPacket(cap, player.getUUID()), player);
    }

    public static void sync(Object msg, PlayerEntity player) {
        sendTo(msg, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player));
    }

}