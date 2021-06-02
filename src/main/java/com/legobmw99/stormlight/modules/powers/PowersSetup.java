package com.legobmw99.stormlight.modules.powers;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.powers.command.StormlightArgType;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.modules.powers.effect.GenericEffect;
import com.legobmw99.stormlight.modules.powers.effect.GravitationEffect;
import com.legobmw99.stormlight.modules.powers.effect.StormlightEffect;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PowersSetup {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Stormlight.MODID);

    public static final RegistryObject<Effect> STORMLIGHT = EFFECTS.register("stormlight", () -> new GenericEffect(EffectType.BENEFICIAL, 0));
    public static final RegistryObject<Effect> SLICKING = EFFECTS.register("slicking", () -> new StormlightEffect(16737535));
    public static final RegistryObject<Effect> STICKING = EFFECTS.register("sticking", () -> new StormlightEffect(6579455));
    public static final RegistryObject<Effect> COHESION = EFFECTS.register("cohesion", () -> new StormlightEffect(16777010));
    public static final RegistryObject<Effect> GRAVITATION = EFFECTS.register("gravitation", GravitationEffect::new);


    public static void register() {
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static void init(final FMLCommonSetupEvent e) {
        ArgumentTypes.register("stormlight_ideal", StormlightArgType.IdealType.class, new ArgumentSerializer<>(() -> StormlightArgType.IdealType.INSTANCE));
        ArgumentTypes.register("stormlight_order", StormlightArgType.OrderType.class, new ArgumentSerializer<>(() -> StormlightArgType.OrderType.INSTANCE));

        SurgebindingCapability.register();
        MinecraftForge.EVENT_BUS.register(PowersEventHandler.class);
    }
}
