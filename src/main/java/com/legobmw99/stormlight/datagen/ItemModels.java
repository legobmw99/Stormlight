package com.legobmw99.stormlight.datagen;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.combat.CombatSetup;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.util.Gemstone;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {
    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Stormlight.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (Order o : Order.values()) {
            itemShardblade(CombatSetup.SHARDBLADES.get(o.getIndex()).get(), "item/" + o.getSingularName());
        }

        for (Gemstone g : Gemstone.values()) {
            itemGenerated(WorldSetup.DUN_SPHERES.get(g.getIndex()).get(), "item/dun_" + g.getName() + "_sphere");
            itemGenerated(WorldSetup.INFUSED_SPHERES.get(g.getIndex()).get(), "item/infused_" + g.getName() + "_sphere");

        }

    }

    public void itemGenerated(Item item, String texture) {
        Stormlight.LOGGER.debug("Creating Item Model for " + item.getRegistryName());
        getBuilder(item.getRegistryName().getPath()).parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", modLoc(texture));
    }

    public void itemHandheld(Item item, String texture) {
        Stormlight.LOGGER.debug("Creating Item Model for " + item.getRegistryName());
        getBuilder(item.getRegistryName().getPath()).parent(getExistingFile(mcLoc("item/handheld"))).texture("layer0", modLoc(texture));
    }

    public void itemShardblade(Item item, String texture) {
        Stormlight.LOGGER.debug("Creating Large Item Model for " + item.getRegistryName());
        getBuilder(item.getRegistryName().getPath()).parent(getExistingFile(modLoc("item/shardblade"))).texture("layer0", modLoc(texture));
    }

    @Override
    public String getName() {
        return "Stormlight Item Models";
    }

}
