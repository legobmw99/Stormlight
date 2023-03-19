package com.legobmw99.stormlight.modules.world;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.powers.block.AdhesionBlock;
import com.legobmw99.stormlight.modules.world.entity.SprenEntity;
import com.legobmw99.stormlight.modules.world.entity.client.SprenModel;
import com.legobmw99.stormlight.modules.world.entity.client.SprenRenderer;
import com.legobmw99.stormlight.modules.world.item.SphereItem;
import com.legobmw99.stormlight.util.Gemstone;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class WorldSetup {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Stormlight.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Stormlight.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Stormlight.MODID);


    public static final List<RegistryObject<SphereItem>> DUN_SPHERES = new ArrayList<>();
    public static final List<RegistryObject<SphereItem>> INFUSED_SPHERES = new ArrayList<>();
    public static final RegistryObject<AdhesionBlock> ADHESION_BLOCK = BLOCKS.register("adhesion_light", AdhesionBlock::new);
    public static final RegistryObject<BlockItem> ADHESION_BLOCK_ITEM = ITEMS.register("adhesion_light",
                                                                                       () -> new BlockItem(ADHESION_BLOCK.get(), new Item.Properties().stacksTo(64)));

    public static final RegistryObject<EntityType<SprenEntity>> SPREN_ENTITY = ENTITIES.register("spren", () -> EntityType.Builder
            .<SprenEntity>of(SprenEntity::new, MobCategory.AMBIENT)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(5)
            .clientTrackingRange(8)
            .setCustomClientFactory((spawnEntity, world) -> new SprenEntity(world, spawnEntity.getEntity()))
            .sized(0.6F, 0.6F)
            .canSpawnFarFromPlayer()
            .build("spren"));

    public static final RegistryObject<SpawnEggItem> SPREN_SPAWN_EGG = ITEMS.register("spren_spawn_egg", () -> new ForgeSpawnEggItem(SPREN_ENTITY, 16382457, 10123246,
                                                                                                                                     new Item.Properties().stacksTo(64)));

    static {
        for (Gemstone gem : Gemstone.values()) {
            String name = gem.getName();
            DUN_SPHERES.add(ITEMS.register("dun_" + name + "_sphere", () -> new SphereItem(false, gem)));
            INFUSED_SPHERES.add(ITEMS.register("infused_" + name + "_sphere", () -> new SphereItem(true, gem)));
        }
    }

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        BLOCKS.register(bus);
        ENTITIES.register(bus);
    }

    public static void registerEntityRenders(final EntityRenderersEvent.RegisterRenderers e) {
        e.registerEntityRenderer(SPREN_ENTITY.get(), SprenRenderer::new);
    }

    public static void registerEntityModels(final EntityRenderersEvent.RegisterLayerDefinitions e) {
        e.registerLayerDefinition(SprenModel.MODEL_LOC, SprenModel::createLayer);
    }

    public static void onEntityAttribute(final net.minecraftforge.event.entity.EntityAttributeCreationEvent e) {
        Stormlight.LOGGER.info("Registering Spren entity attributes");
        e.put(SPREN_ENTITY.get(), SprenEntity.createAttributes());
    }
}
