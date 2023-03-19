package com.legobmw99.stormlight.network;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.api.ISurgebindingData;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.network.packets.SummonBladePacket;
import com.legobmw99.stormlight.network.packets.SurgePacket;
import com.legobmw99.stormlight.network.packets.SurgebindingDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Network {

    private static final String VERSION = "1.1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Stormlight.MODID, "networking"), () -> VERSION, VERSION::equals,
                                                                                  VERSION::equals);

    private static int index = 0;

    private static int nextIndex() {
        return index++;
    }

    public static void registerPackets() {

        INSTANCE.registerMessage(nextIndex(), SurgebindingDataPacket.class, SurgebindingDataPacket::encode, SurgebindingDataPacket::decode, SurgebindingDataPacket::handle);
        INSTANCE.registerMessage(nextIndex(), SummonBladePacket.class, (self, buf) -> {}, buf -> new SummonBladePacket(), SummonBladePacket::handle);
        INSTANCE.registerMessage(nextIndex(), SurgePacket.class, SurgePacket::encode, SurgePacket::decode, SurgePacket::handle);

    }

    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }

    public static void sendTo(Object msg, ServerPlayer player) {
        if (!(player instanceof FakePlayer)) {
            INSTANCE.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static void sendTo(Object msg, PacketDistributor.PacketTarget target) {
        INSTANCE.send(target, msg);
    }

    public static void sync(Player player) {
        player.getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(data -> sync(data, player));
    }

    public static void sync(ISurgebindingData cap, Player player) {
        sync(new SurgebindingDataPacket(cap, player), player);
    }

    public static void sync(Object msg, Player player) {
        sendTo(msg, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player));
    }

}