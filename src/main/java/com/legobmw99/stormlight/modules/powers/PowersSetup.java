package com.legobmw99.stormlight.modules.powers;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.powers.command.StormlightArgType;
import com.legobmw99.stormlight.modules.powers.effect.GenericEffect;
import com.legobmw99.stormlight.modules.powers.effect.StormlightEffect;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PowersSetup {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Stormlight.MODID);

    public static final RegistryObject<MobEffect> STORMLIGHT = EFFECTS.register("stormlight", () -> new GenericEffect(MobEffectCategory.BENEFICIAL, 0));
    public static final RegistryObject<MobEffect> SLICKING = EFFECTS.register("slicking", () -> new StormlightEffect(16737535));
    public static final RegistryObject<MobEffect> STICKING = EFFECTS.register("sticking", () -> new StormlightEffect(6579455));
    public static final RegistryObject<MobEffect> COHESION = EFFECTS.register("cohesion", () -> new StormlightEffect(16777010));
    public static final RegistryObject<MobEffect> TENSION = EFFECTS.register("tension", () -> new StormlightEffect(16711780));
    public static final RegistryObject<MobEffect> GRAVITATION = EFFECTS.register("gravitation",
                                                                                 () -> new StormlightEffect(6605055).addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(),
                                                                                                                                          "a81758d2-c355-11eb-8529-0242ac130003",
                                                                                                                                          -0.08,
                                                                                                                                          AttributeModifier.Operation.ADDITION));


    public static void register() {
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static void init(final FMLCommonSetupEvent e) {
        ArgumentTypes.register("stormlight_ideal", StormlightArgType.IdealType.class, new EmptyArgumentSerializer<>(() -> StormlightArgType.IdealType.INSTANCE));
        ArgumentTypes.register("stormlight_order", StormlightArgType.OrderType.class, new EmptyArgumentSerializer<>(() -> StormlightArgType.OrderType.INSTANCE));

        MinecraftForge.EVENT_BUS.register(PowersEventHandler.class);
    }
}
