package com.legobmw99.stormlight;

import com.legobmw99.stormlight.modules.combat.CombatSetup;
import com.legobmw99.stormlight.modules.powers.PowersSetup;
import com.legobmw99.stormlight.modules.powers.client.PowersClientSetup;
import com.legobmw99.stormlight.modules.powers.command.StormlightCommand;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.network.Network;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Stormlight.MODID)
public class Stormlight {
    public static final String MODID = "stormlight";
    public static final Logger LOGGER = LogManager.getLogger();

    public static Stormlight instance;

    public Stormlight() {
        instance = this;
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(Stormlight::init);
        modBus.addListener(Stormlight::clientInit);
        modBus.addListener(WorldSetup::onEntityAttribute);
        modBus.addListener(SurgebindingCapability::registerCapability);
        modBus.addListener(WorldSetup::registerEntityModels);
        modBus.addListener(WorldSetup::registerEntityRenders);
        modBus.addListener(Stormlight::registerCreativeTabs);


        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modBus.addListener(PowersClientSetup::registerKeyBinding);
        });


        MinecraftForge.EVENT_BUS.addListener(Stormlight::registerCommands);

        PowersSetup.register();
        WorldSetup.register();
        CombatSetup.register();
    }

    public static void clientInit(final FMLClientSetupEvent e) {
        PowersClientSetup.clientInit(e);
        CombatSetup.clientInit(e);
    }

    public static void registerCommands(final RegisterCommandsEvent e) {
        StormlightCommand.register(e.getDispatcher());
    }

    public static void init(final FMLCommonSetupEvent e) {
        PowersSetup.init(e);
        e.enqueueWork(Network::registerPackets);
    }


    public static CreativeModeTab stormlight_group;

    public static void registerCreativeTabs(CreativeModeTabEvent.Register event) {
        stormlight_group = event.registerCreativeModeTab(new ResourceLocation(MODID, "main_tab"), builder -> builder.icon(() -> {
            int type = (int) (System.currentTimeMillis() / (1000 * 60)) % 10;
            return new ItemStack(CombatSetup.SHARDBLADES.get(type).get());
        }).title(Component.translatable("tabs.stormlight.main_tab")).displayItems((featureFlags, output) -> {
            for (var shardblade : CombatSetup.SHARDBLADES) {
                output.accept(shardblade.get());
            }
            for (var sphere : WorldSetup.DUN_SPHERES) {
                output.accept(sphere.get());
            }
            for (var sphere : WorldSetup.INFUSED_SPHERES) {
                output.accept(sphere.get());
            }
            output.accept(WorldSetup.SPREN_SPAWN_EGG.get());

        }));
    }


}
