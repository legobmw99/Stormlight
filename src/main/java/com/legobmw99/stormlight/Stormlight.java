package com.legobmw99.stormlight;

import com.legobmw99.stormlight.modules.combat.CombatSetup;
import com.legobmw99.stormlight.modules.powers.PowersSetup;
import com.legobmw99.stormlight.modules.powers.client.PowersClientSetup;
import com.legobmw99.stormlight.modules.powers.command.StormlightCommand;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.network.Network;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
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

    public static final CreativeModeTab stormlight_group = new CreativeModeTab(Stormlight.MODID) {
        @Override
        public ItemStack makeIcon() {
            int type = (int) (System.currentTimeMillis() / (1000 * 60)) % 10;
            return new ItemStack(CombatSetup.SHARDBLADES.get(type).get());
        }
    };

    public static Stormlight instance;

    public Stormlight() {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Stormlight::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Stormlight::clientInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(WorldSetup::onEntityAttribute);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SurgebindingCapability::registerCapability);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(WorldSetup::registerEntityModels);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(WorldSetup::registerEntityRenders);
        MinecraftForge.EVENT_BUS.addListener(Stormlight::registerCommands);

        PowersSetup.register();
        WorldSetup.register();
        CombatSetup.register();
    }

    public static Item.Properties createStandardItemProperties() {
        return new Item.Properties().tab(stormlight_group).stacksTo(64);
    }

    public static void clientInit(final FMLClientSetupEvent e) {
        PowersClientSetup.clientInit(e);
        WorldSetup.clientInit(e);
        CombatSetup.clientInit(e);
    }

    public static void registerCommands(final RegisterCommandsEvent e) {
        StormlightCommand.register(e.getDispatcher());
    }

    public static void init(final FMLCommonSetupEvent e) {
        PowersSetup.init(e);
        e.enqueueWork(Network::registerPackets);
    }

}
