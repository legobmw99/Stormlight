package com.legobmw99.stormlight.modules.world;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.powers.block.AdhesionBlock;
import com.legobmw99.stormlight.modules.world.entity.SprenEntity;
import com.legobmw99.stormlight.modules.world.entity.client.SprenRenderer;
import com.legobmw99.stormlight.modules.world.item.SphereItem;
import com.legobmw99.stormlight.util.Gemstone;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class WorldSetup {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Stormlight.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Stormlight.MODID);


    public static final List<RegistryObject<SphereItem>> DUN_SPHERES = new ArrayList<>();
    public static final List<RegistryObject<SphereItem>> INFUSED_SPHERES = new ArrayList<>();

    public static final EntityType<SprenEntity> SPREN_ENTITY = EntityType.Builder
            .<SprenEntity>of(SprenEntity::new, EntityClassification.AMBIENT)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(5)
            .setCustomClientFactory((spawnEntity, world) -> new SprenEntity(world, spawnEntity.getEntity()))
            .sized(0.6F, 0.6F)
            .canSpawnFarFromPlayer()
            .build("spren");

    public static final RegistryObject<SpawnEggItem> SPREN_SPAWN_EGG = ITEMS.register("spren_spawn_egg", () -> new SpawnEggItem(SPREN_ENTITY, 16382457, 10123246,
                                                                                                                                Stormlight.createStandardItemProperties()));
    public static final RegistryObject<AdhesionBlock> ADHESION_BLOCK = BLOCKS.register("adhesion_light", AdhesionBlock::new);
    public static final RegistryObject<BlockItem> ADHESION_BLOCK_ITEM = ITEMS.register("adhesion_light",
                                                                                       () -> new BlockItem(ADHESION_BLOCK.get(), Stormlight.createStandardItemProperties()));

    static {
        for (Gemstone gem : Gemstone.values()) {
            String name = gem.getName();
            DUN_SPHERES.add(ITEMS.register("dun_" + name + "_sphere", () -> new SphereItem(false, gem)));
            INFUSED_SPHERES.add(ITEMS.register("infused_" + name + "_sphere", () -> new SphereItem(true, gem)));
        }
    }

    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit(final FMLClientSetupEvent e) {
        RenderTypeLookup.setRenderLayer(ADHESION_BLOCK.get(), RenderType.translucent());
        registerEntityRenders();
    }


    public static void onEntityAttribute(final net.minecraftforge.event.entity.EntityAttributeCreationEvent e) {
        Stormlight.LOGGER.info("Registering Spren entity attributes");
        e.put(SPREN_ENTITY, SprenEntity.createAttributes());
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(SPREN_ENTITY, manager -> new SprenRenderer(manager));
    }

    public static void onEntityRegister(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(SPREN_ENTITY.setRegistryName(Stormlight.MODID, "spren"));
        EntitySpawnPlacementRegistry.register(SPREN_ENTITY, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING,
                                              SprenEntity::checkSprenSpawnRules);
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
