package com.legobmw99.stormlight.datagen;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        // var lookup = event.getLookupProvider();
        var fileHelper = event.getExistingFileHelper();
        generator.addProvider(event.includeServer(), new Recipes(packOutput));
        // BlockTags blocktags = new BlockTags(packOutput, lookup, fileHelper);
        // generator.addProvider(event.includeServer(), blocktags);
        // generator.addProvider(event.includeServer(), new ItemTags(packOutput, lookup, blocktags, fileHelper));
        // generator.addProvider(event.includeServer(), new ForgeAdvancementProvider(packOutput, lookup, fileHelper, List.of(new Advancements())));
        generator.addProvider(event.includeClient(), new Languages(packOutput));
        generator.addProvider(event.includeClient(), new BlockStates(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new ItemModels(packOutput, fileHelper));
    }
}