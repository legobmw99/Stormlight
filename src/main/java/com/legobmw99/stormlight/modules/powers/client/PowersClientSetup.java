package com.legobmw99.stormlight.modules.powers.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

public class PowersClientSetup {
    public static KeyBinding firstSurge;
    public static KeyBinding secondSurge;
    public static KeyBinding blade;


    public static void clientInit(final FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.register(PowerClientEventHandler.class);
        initKeybindings();
    }


    public static void initKeybindings() {
        firstSurge = new KeyBinding("key.firstSurge", GLFW.GLFW_KEY_F, "key.categories.stormlight");
        secondSurge = new KeyBinding("key.secondSurge", GLFW.GLFW_KEY_G, "key.categories.stormlight");
        blade = new KeyBinding("key.blade", GLFW.GLFW_KEY_V, "key.categories.stormlight");
        ClientRegistry.registerKeyBinding(firstSurge);
        ClientRegistry.registerKeyBinding(secondSurge);
        ClientRegistry.registerKeyBinding(blade);
    }
}
