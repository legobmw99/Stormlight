package com.legobmw99.stormlight.modules.powers.block;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class AdhesionBlock extends FaceAttachedHorizontalDirectionalBlock {

    private static final VoxelShape TOP = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.5D, 16.0D);
    private static final VoxelShape BOTTOM = Block.box(0.0D, 15.5D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape NORTH = Block.box(0.0D, 0.0D, 15.5D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SOUTH = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 0.5D);
    private static final VoxelShape EAST = Block.box(0.0D, 0.0D, 0.0D, 0.5D, 16.0D, 16.0D);
    private static final VoxelShape WEST = Block.box(15.5D, .0D, 0.0D, 16.0D, 16.0D, 16.0D);


    public AdhesionBlock() {
        super(BlockBehaviour.Properties
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

    public static AttachFace fromDirection(Direction dir) {
        switch (dir) {
            case UP:
                return AttachFace.CEILING;
            case DOWN:
                return AttachFace.FLOOR;
            default:
                return AttachFace.WALL;
        }
    }


    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    public int coat(Level world, BlockPos pos) {
        int sides = 0;
        for (Direction d : Direction.values()) {
            if (canAttachFrom(world, pos, d) && world.getBlockState(pos.relative(d)).isAir()/*.getMaterial().isReplaceable()*/) {
                AttachFace face = fromDirection(d.getOpposite());
                BlockState newBlock = defaultBlockState().setValue(FACE, face);
                if (face == AttachFace.WALL) {
                    newBlock = newBlock.setValue(FACING, d);
                }
                world.setBlockAndUpdate(pos.relative(d), newBlock);
                sides++;
            }
        }
        return sides;
    }

    public boolean canAttachFrom(Level world, BlockPos pos, Direction d) {
        return canAttach(world, pos.relative(d), d.getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
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
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return Shapes.empty();
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return Shapes.empty();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, BlockGetter reader, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE);
    }


    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }


    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (!entity.isNoGravity() && (entity instanceof LivingEntity && !((LivingEntity) entity).hasEffect(PowersSetup.GRAVITATION.get()))) {
            Vec3 target;

            switch (state.getValue(FACE)) {
                case FLOOR:
                    target = Vec3.atBottomCenterOf(pos);
                    break;
                case CEILING:
                    target = Vec3.atBottomCenterOf(pos.above());
                    break;
                case WALL:
                default:
                    switch (state.getValue(FACING)) {
                        case EAST:
                            target = Vec3.atCenterOf(pos.below().west());
                            break;
                        case WEST:
                            target = Vec3.atCenterOf(pos.below().east());
                            break;
                        case SOUTH:
                            target = Vec3.atCenterOf(pos.below().north());
                            break;
                        case NORTH:
                        default:
                            target = Vec3.atCenterOf(pos.below().south());
                            break;
                    }
            }
            entity.setDeltaMovement(target.subtract(entity.position()).normalize());
            entity.makeStuckInBlock(state, new Vec3(0.01, 1, 0.01));
        }
    }
}
