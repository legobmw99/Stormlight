package com.legobmw99.stormlight.modules.powers;

import com.legobmw99.stormlight.Stormlight;
import net.minecraft.potion.Effect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PowersSetup {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Stormlight.MODID);

    public static final RegistryObject<Effect> STORMLIGHT = EFFECTS.register("stormlight", StormlightEffect::new);

    public static void register() {
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static void init(final FMLCommonSetupEvent e) {
        StormlightCapability.register();
        MinecraftForge.EVENT_BUS.register(PowersEventHandler.class);
    }
}
