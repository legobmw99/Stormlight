package com.legobmw99.stormlight.modules.powers;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.powers.command.StormlightArgType;
import com.legobmw99.stormlight.modules.powers.effect.GenericEffect;
import com.legobmw99.stormlight.modules.powers.effect.StormlightEffect;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, Stormlight.MODID);
    private static final RegistryObject<SingletonArgumentInfo<StormlightArgType.IdealType>> COMMAND_IDEAL_TYPE = COMMAND_ARGUMENT_TYPES.register("stormlight_ideal",
                                                                                                                                                 () -> ArgumentTypeInfos.registerByClass(
                                                                                                                                                         StormlightArgType.IdealType.class,
                                                                                                                                                         SingletonArgumentInfo.contextFree(
                                                                                                                                                                 StormlightArgType.IdealType::new)));

    private static final RegistryObject<SingletonArgumentInfo<StormlightArgType.OrderType>> COMMAND_ORDER_TYPE = COMMAND_ARGUMENT_TYPES.register("stormlight_order",
                                                                                                                                                 () -> ArgumentTypeInfos.registerByClass(
                                                                                                                                                         StormlightArgType.OrderType.class,
                                                                                                                                                         SingletonArgumentInfo.contextFree(
                                                                                                                                                                 StormlightArgType.OrderType::new)));

    public static void register() {
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        COMMAND_ARGUMENT_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static void init(final FMLCommonSetupEvent e) {
        e.enqueueWork(() -> MinecraftForge.EVENT_BUS.register(PowersEventHandler.class));

    }
}
