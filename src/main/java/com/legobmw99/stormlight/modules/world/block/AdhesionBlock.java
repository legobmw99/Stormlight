package com.legobmw99.stormlight.modules.world.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class AdhesionBlock extends HorizontalFaceBlock {

    private static final VoxelShape TOP = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape BOTTOM = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape NORTH = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SOUTH = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape EAST = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST = Block.box(15.0D, .0D, 0.0D, 16.0D, 16.0D, 16.0D);


    public AdhesionBlock() {
        super(AbstractBlock.Properties
                      .of(Material.TOP_SNOW)
                      .lightLevel((d) -> 15)
                      .jumpFactor(0.0F)
                      .noDrops()
                      .strength(-1.0F, 3600000.0F)
                      .randomTicks()
                      .speedFactor(0.0F)
                      .noOcclusion()
                      .isValidSpawn((a, b, c, d) -> false)
                      .isRedstoneConductor((a, b, c) -> false)
                      .isSuffocating((a, b, c) -> false)
                      .isViewBlocking((a, b, c) -> false)
                      .sound(SoundType.GLASS));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL));

    }


    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
        switch (state.getValue(FACE)) {
            case FLOOR:
                return TOP;
            case CEILING:
                return BOTTOM;
            case WALL:
            default:
                switch (state.getValue(FACING)) {
                    case EAST:
                        return EAST;
                    case WEST:
                        return WEST;
                    case SOUTH:
                        return SOUTH;
                    case NORTH:
                    default:
                        return NORTH;
                }
        }
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE);
    }


    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }


    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!entity.isNoGravity()) {
            Vector3d target;

            switch (state.getValue(FACE)) {
                case FLOOR:
                    target = Vector3d.atBottomCenterOf(pos);
                    break;
                case CEILING:
                    target = Vector3d.atBottomCenterOf(pos.above());
                    break;
                case WALL:
                default:
                    switch (state.getValue(FACING)) {
                        case EAST:
                            target = Vector3d.atCenterOf(pos.below().west());
                            break;
                        case WEST:
                            target = Vector3d.atCenterOf(pos.below().east());
                            break;
                        case SOUTH:
                            target = Vector3d.atCenterOf(pos.below().north());
                            break;
                        case NORTH:
                        default:
                            target = Vector3d.atCenterOf(pos.below().south());
                            break;
                    }
            }
            entity.setDeltaMovement(target.subtract(entity.position()).normalize());
            entity.makeStuckInBlock(state, new Vector3d(0.01, 1, 0.01));
        }
    }
}
