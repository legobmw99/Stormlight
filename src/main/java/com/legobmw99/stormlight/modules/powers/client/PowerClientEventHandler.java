package com.legobmw99.stormlight.modules.powers.client;

import com.legobmw99.stormlight.modules.powers.StormlightCapability;
import com.legobmw99.stormlight.network.Network;
import com.legobmw99.stormlight.network.packets.SummonBladePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
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
            StormlightCapability cap;
            if (this.mc.screen == null) {
                if (player == null || !this.mc.isWindowActive()) {
                    return;
                }
                cap = StormlightCapability.forPlayer(player);
                if (PowersClientSetup.blade.isDown() && cap.isBladeStored()) {
                    System.out.println("blade");
                    Network.sendToServer(new SummonBladePacket());
                }
            }
        }
    }
}
