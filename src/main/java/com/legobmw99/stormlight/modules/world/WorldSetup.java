package com.legobmw99.stormlight.modules.world;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.powers.StormlightCapability;
import com.legobmw99.stormlight.modules.world.item.SphereItem;
import com.legobmw99.stormlight.util.Gemstone;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class WorldSetup {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Stormlight.MODID);


    public static final List<RegistryObject<SphereItem>> DUN_SPHERES = new ArrayList<>();
    public static final List<RegistryObject<SphereItem>> INFUSED_SPHERES = new ArrayList<>();

    static{
        for (Gemstone gem : Gemstone.values()) {
            String name = gem.getName();
            DUN_SPHERES.add(ITEMS.register("dun_"+name+"_sphere",  () -> new SphereItem(false, gem)));
            INFUSED_SPHERES.add(ITEMS.register("infused_"+name+"_sphere",  () -> new SphereItem(true, gem)));
        }
    }

    public static void register(){
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void clientInit(final FMLClientSetupEvent e) {
        //WorldClientSetup.registerEntityRenders();
    }


    /*public static void registerEntities() {
        int id = 1;
        EntityRegistry.registerModEntity(new ResourceLocation(Stormlight.MODID, "Spren"), EntitySpren.class, "Spren",
                                         id++, Stormlight.instance, 64, 3, true, 0xEDE2FF, 0xC698CE);

        EntityRegistry.addSpawn(EntitySpren.class, 25, 1, 1, EnumCreatureType.CREATURE, Biomes.PLAINS,
                                Biomes.ICE_PLAINS, Biomes.TAIGA, Biomes.FOREST, Biomes.DESERT, Biomes.JUNGLE, Biomes.MESA,
                                Biomes.SAVANNA, Biomes.EXTREME_HILLS, Biomes.SWAMPLAND, Biomes.MUSHROOM_ISLAND);
    }*/

}
