package com.legobmw99.stormlight.datagen;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.util.Gemstone;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemModels extends ItemModelProvider {
    public ItemModels(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, Stormlight.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        parentedBlock(WorldSetup.ADHESION_BLOCK.get(), "block/adhesion_light");

        for (Order o : Order.values()) {
            itemShield(o, "item/" + o.getSingularName() + "_shield");
            itemShardblade(o, "item/" + o.getSingularName() + "_shardblade");
        }

        for (Gemstone g : Gemstone.values()) {
            itemGenerated(WorldSetup.DUN_SPHERES.get(g.getIndex()).get(), "item/dun_" + g.getName() + "_sphere");
            itemGenerated(WorldSetup.INFUSED_SPHERES.get(g.getIndex()).get(), "item/infused_" + g.getName() + "_sphere");

        }

        getBuilder(ForgeRegistries.ITEMS.getKey(WorldSetup.SPREN_SPAWN_EGG.get()).getPath()).parent(getExistingFile(mcLoc("item/template_spawn_egg")));


    }

    public void itemGenerated(Item item, String texture) {
        Stormlight.LOGGER.debug("Creating Item Model for " + ForgeRegistries.ITEMS.getKey(item));
        getBuilder(ForgeRegistries.ITEMS.getKey(item).getPath()).parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", modLoc(texture));
    }

    public void parentedBlock(Block block, String model) {
        Stormlight.LOGGER.debug("Creating Item Model for " + ForgeRegistries.BLOCKS.getKey(block));
        getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath()).parent(new ModelFile.UncheckedModelFile(modLoc(model)));
    }

    public void itemHandheld(Item item, String texture) {
        Stormlight.LOGGER.debug("Creating Item Model for " + ForgeRegistries.ITEMS.getKey(item));
        getBuilder(ForgeRegistries.ITEMS.getKey(item).getPath()).parent(getExistingFile(mcLoc("item/handheld"))).texture("layer0", modLoc(texture));
    }

    public void itemShardblade(Order order, String texture) {
        Stormlight.LOGGER.debug("Creating Shardblade Model for " + order.getSingularName());
        getBuilder(order.getSingularName() + "_shardblade").parent(getExistingFile(modLoc("item/shardblade")))
                .texture("layer0", modLoc(texture))
                .override()
                .predicate(new ResourceLocation(Stormlight.MODID, "shielding"), 1.0F)
                .model(getExistingFile(modLoc("item/" + order.getSingularName() + "_shield")));
    }

    public void itemShield(Order order, String texture) {
        Stormlight.LOGGER.debug("Creating Shield Model for " + order.getSingularName());
        getBuilder(order.getSingularName() + "_shield").parent(getExistingFile(modLoc("item/shield"))).texture("layer0", modLoc(texture));
    }

    @Override
    public String getName() {
        return "Stormlight Item Models";
    }

}
