package com.legobmw99.stormlight.datagen;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class BlockStates extends BlockStateProvider {
    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Stormlight.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile layer = models()
                .withExistingParent("stormlight:adhesion_light", mcLoc("block/snow_height2"))
                .texture("particle", blockTexture(Blocks.SEA_LANTERN))
                .texture("texture", blockTexture(Blocks.LIGHT_BLUE_STAINED_GLASS));

        simpleBlock(WorldSetup.ADHESION_BLOCK.get(), layer);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Stormlight blockstates";
    }
}

