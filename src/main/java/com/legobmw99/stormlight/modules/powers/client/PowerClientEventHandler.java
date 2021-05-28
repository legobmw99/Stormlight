package com.legobmw99.stormlight.modules.powers.client;

import com.legobmw99.stormlight.modules.combat.item.ShardbladeItem;
import com.legobmw99.stormlight.modules.powers.PowersSetup;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.network.Network;
import com.legobmw99.stormlight.network.packets.SummonBladePacket;
import com.legobmw99.stormlight.network.packets.SurgePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class PowerClientEventHandler {

    private final Minecraft mc = Minecraft.getInstance();


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        acceptInput(event.getAction());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onMouseInput(final InputEvent.MouseInputEvent event) {
        acceptInput(event.getAction());

    }

    /**
     * Handles either mouse or button presses for the mod's keybinds
     */
    private void acceptInput(int action) {
        if (action == GLFW.GLFW_PRESS) {// || action == GLFW.GLFW_REPEAT){
            PlayerEntity player = this.mc.player;
            if (this.mc.screen == null) {
                if (player == null || !this.mc.isWindowActive()) {
                    return;
                }
                player.getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(data -> {

                    if (data.isKnight()) {

                        if (PowersClientSetup.blade.isDown() && (data.isBladeStored() || player.getMainHandItem().getItem() instanceof ShardbladeItem)) {
                            Network.sendToServer(new SummonBladePacket());
                            PowersClientSetup.blade.setDown(false);
                        }

                        if (player.hasEffect(PowersSetup.STORMLIGHT.get())) {
                            if (PowersClientSetup.firstSurge.isDown()) {

                                RayTraceResult ray = player.pick(30f, Minecraft.getInstance().getFrameTime(), false);
                                Network.sendToServer(
                                        new SurgePacket(data.getOrder().getFirst(), new BlockPos(ray.getLocation()), Minecraft.getInstance().options.keyShift.isDown()));
                            }

                            if (PowersClientSetup.secondSurge.isDown()) {
                                RayTraceResult ray = player.pick(30f, Minecraft.getInstance().getFrameTime(), false);
                                Network.sendToServer(
                                        new SurgePacket(data.getOrder().getSecond(), new BlockPos(ray.getLocation()), Minecraft.getInstance().options.keyShift.isDown()));
                            }
                        }
                    }
                });
            }
        }
    }


    //    @OnlyIn(Dist.CLIENT)
    //    @SubscribeEvent
    //    public void onClientTick(final TickEvent.ClientTickEvent event) {
    //        // Run once per tick, only if in game, and only if there is a player
    //
    //        // TODO should this be part of acceptInput above
    //
    //        if (event.phase == TickEvent.Phase.END && !this.mc.isPaused() && this.mc.player != null && this.mc.player.isAlive()) {
    //
    //            PlayerEntity player = this.mc.player;
    //            player.getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(cap -> {
    //                if (cap.isKnight()) {
    //                    if (player.hasEffect(PowersSetup.STORMLIGHT.get())) {
    //                        if (PowersClientSetup.firstSurge.isDown()) {
    //
    //                            RayTraceResult ray = player.pick(30f, Minecraft.getInstance().getFrameTime(), false);
    //                            Network.sendToServer(new SurgePacket(cap.getOrder().getFirst(), new BlockPos(ray.getLocation()), Minecraft.getInstance().options.keyShift.isDown()));
    //                        }
    //
    //                        if (PowersClientSetup.secondSurge.isDown()) {
    //                            RayTraceResult ray = player.pick(30f, Minecraft.getInstance().getFrameTime(), false);
    //                            Network.sendToServer(new SurgePacket(cap.getOrder().getSecond(), new BlockPos(ray.getLocation()), Minecraft.getInstance().options.keyShift.isDown()));
    //                        }
    //                    }
    //                }
    //            });
    //
    //        }
    //    }
}
