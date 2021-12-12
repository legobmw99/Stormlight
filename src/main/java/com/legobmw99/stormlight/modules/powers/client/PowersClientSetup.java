package com.legobmw99.stormlight.modules.powers.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

public class PowersClientSetup {
    public static KeyMapping firstSurge;
    public static KeyMapping secondSurge;
    public static KeyMapping blade;


    public static void clientInit(final FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.register(PowerClientEventHandler.class);
        initKeybindings();
    }


    public static void initKeybindings() {
        firstSurge = new KeyMapping("key.firstSurge", GLFW.GLFW_KEY_F, "key.categories.stormlight");
        secondSurge = new KeyMapping("key.secondSurge", GLFW.GLFW_KEY_G, "key.categories.stormlight");
        blade = new KeyMapping("key.blade", GLFW.GLFW_KEY_V, "key.categories.stormlight");
        ClientRegistry.registerKeyBinding(firstSurge);
        ClientRegistry.registerKeyBinding(secondSurge);
        ClientRegistry.registerKeyBinding(blade);
    }
}
