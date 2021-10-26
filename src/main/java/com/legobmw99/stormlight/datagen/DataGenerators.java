package com.legobmw99.stormlight.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new Recipes(generator));
            //generator.addProvider(new LootTables(generator));
            //BlockTags blocktags = new BlockTags(generator, Allomancy.MODID, event.getExistingFileHelper());
            //generator.addProvider(blocktags);
            //generator.addProvider(new ItemTags(generator, blocktags, Stormlight.MODID, event.getExistingFileHelper()));
            //generator.addProvider(new Advancements(generator));
        }
        if (event.includeClient()) {
            generator.addProvider(new Languages(generator));
            generator.addProvider(new BlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(new ItemModels(generator, event.getExistingFileHelper()));
        }
    }
}