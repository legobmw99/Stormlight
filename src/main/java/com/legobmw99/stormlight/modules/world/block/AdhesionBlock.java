package com.legobmw99.stormlight.modules.world.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class AdhesionBlock extends Block {

    public AdhesionBlock() {
        super(AbstractBlock.Properties
                      .of(Material.TOP_SNOW)
                      .lightLevel((d) -> 13)
                      .jumpFactor(0.0F)
                      .noDrops()
                      .strength(-1.0F, 3600000.0F)
                      .randomTicks()
                      .speedFactor(-0.00001F)
                      .noOcclusion()
                      .isValidSpawn((a, b, c, d) -> false)
                      .isRedstoneConductor((a, b, c) -> false)
                      .isSuffocating((a, b, c) -> false)
                      .isViewBlocking((a, b, c) -> false)
                      .sound(SoundType.GLASS));
    }


    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
        return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
        return VoxelShapes.empty();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, IBlockReader reader, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        BlockState blockstate = world.getBlockState(pos.below());
        if (!blockstate.is(Blocks.BARRIER)) {
            return Block.isFaceFull(blockstate.getCollisionShape(world, pos.below()), Direction.UP);
        }
        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2) {
        return !state.canSurvive(world, pos1) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, state2, world, pos1, pos2);
    }


    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }


    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!entity.isNoGravity()) {
            entity.setDeltaMovement(Vector3d.atBottomCenterOf(pos).subtract(entity.position()).normalize());
            entity.makeStuckInBlock(state, new Vector3d(0.01, 1, 0.01));
        }
    }
}
