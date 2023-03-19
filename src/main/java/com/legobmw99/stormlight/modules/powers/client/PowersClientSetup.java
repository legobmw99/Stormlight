package com.legobmw99.stormlight.modules.powers.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

public class PowersClientSetup {
    public static KeyMapping firstSurge;
    public static KeyMapping secondSurge;
    public static KeyMapping blade;


    public static void clientInit(final FMLClientSetupEvent e) {
        e.enqueueWork(() -> MinecraftForge.EVENT_BUS.register(PowerClientEventHandler.class));
    }


    public static void registerKeyBinding(final RegisterKeyMappingsEvent evt) {
        firstSurge = new KeyMapping("key.firstSurge", GLFW.GLFW_KEY_F, "key.categories.stormlight");
        secondSurge = new KeyMapping("key.secondSurge", GLFW.GLFW_KEY_G, "key.categories.stormlight");
        blade = new KeyMapping("key.blade", GLFW.GLFW_KEY_V, "key.categories.stormlight");
        evt.register(firstSurge);
        evt.register(secondSurge);
        evt.register(blade);
    }
}
