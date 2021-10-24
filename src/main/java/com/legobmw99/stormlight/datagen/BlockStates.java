package com.legobmw99.stormlight.datagen;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.powers.block.AdhesionBlock;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class BlockStates extends BlockStateProvider {
    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Stormlight.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        createAdhesionLayer();
    }

    private void createAdhesionLayer() {
        ModelFile layer = models()
                .withExistingParent("stormlight:adhesion_light", modLoc("block/light"))
                .texture("texture", modLoc("block/adhesion_light"))
                .texture("particle", blockTexture(Blocks.SEA_LANTERN));

        VariantBlockStateBuilder builder = getVariantBuilder(WorldSetup.ADHESION_BLOCK.get());

        for (AttachFace face : AdhesionBlock.FACE.getPossibleValues()) {
            int xangle = (face == AttachFace.CEILING) ? 180 : (face == AttachFace.WALL) ? 90 : 0;
            boolean uvlock = face == AttachFace.WALL;
            for (Direction dir : AdhesionBlock.FACING.getPossibleValues()) {
                int yangle = (int) dir.toYRot();
                yangle = face != AttachFace.CEILING ? (yangle + 180) % 360 : yangle;
                builder
                        .partialState()
                        .with(AdhesionBlock.FACE, face)
                        .with(AdhesionBlock.FACING, dir)
                        .modelForState()
                        .modelFile(layer)
                        .uvLock(uvlock)
                        .rotationY(yangle)
                        .rotationX(xangle)
                        .addModel();
            }
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return "Stormlight blockstates";
    }
}

