package com.legobmw99.stormlight;

import com.legobmw99.stormlight.modules.combat.CombatSetup;
import com.legobmw99.stormlight.modules.powers.PowersSetup;
import com.legobmw99.stormlight.modules.powers.client.PowersClientSetup;
import com.legobmw99.stormlight.modules.powers.command.StormlightArgType;
import com.legobmw99.stormlight.modules.powers.command.StormlightCommand;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.network.Network;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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

    public static final ItemGroup stormlight_group = new ItemGroup(Stormlight.MODID) {
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
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(EntityType.class, WorldSetup::onEntityRegister);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(WorldSetup::onEntityAttribute);
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
        Network.registerPackets();
    }


}
