package com.legobmw99.stormlight.modules.powers;

import com.legobmw99.stormlight.modules.powers.container.PortableCraftingContainer;
import com.legobmw99.stormlight.modules.powers.container.PortableStonecutterContainer;
import com.legobmw99.stormlight.modules.powers.effect.EffectHelper;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.network.Network;
import com.legobmw99.stormlight.network.packets.SurgePacket;
import com.legobmw99.stormlight.util.Surge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Surges {

    private static Direction DIRECTIONS[] = {Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    private static boolean isBlockSafe(BlockPos pos, World level) {
        return pos != null && level.isLoaded(pos) && !level.getBlockState(pos).isAir();
    }

    private static BlockPos findAdjacentBlock(BlockPos pos, ServerPlayerEntity player) {
        for (Direction d : DIRECTIONS) {
            BlockPos adj = pos.relative(d);
            if (!player.level.getBlockState(adj).isSuffocating(player.level, adj)) {
                return adj;
            }
        }
        return null;
    }

    private static final Map<Block, Block> transformableBlocks = buildBlockMap();

    private static Map<Block,Block>  buildBlockMap() {
        Map<Block,Block> map = new HashMap<Block,Block>();
        map.put(Blocks.COBBLESTONE, Blocks.STONE);
        map.put(Blocks.SANDSTONE, Blocks.RED_SANDSTONE);
        map.put(Blocks.GRASS_BLOCK, Blocks.MYCELIUM);
        map.put(Blocks.OBSIDIAN, Blocks.LAVA);
        map.put(Blocks.STONE, Blocks.HAY_BLOCK);
        map.put(Blocks.MELON, Blocks.PUMPKIN);
        map.put(Blocks.PUMPKIN, Blocks.MELON);
        map.put(Blocks.WHITE_WOOL, Blocks.COBWEB);
        return map;
    }

    public static void adhesion(ServerPlayerEntity player, BlockPos pos, boolean shiftHeld) {
        if (shiftHeld) {
            if (player.hasEffect(PowersSetup.TENSION.get())) {
                ItemStack stack = player.getMainHandItem();
                if (stack.isDamaged()) {
                    if (EffectHelper.drainStormlight(player, 1000)) {
                        stack.setDamageValue((int) (stack.getDamageValue() - 20 * stack.getXpRepairRatio()));
                    } else if (EffectHelper.drainStormlight(player, 200)) {
                        stack.setDamageValue((int) (stack.getDamageValue() - 2 * stack.getXpRepairRatio()));

                    }
                }
            }
        } else if (isBlockSafe(pos, player.level)) {
            if (player.getEffect(PowersSetup.STORMLIGHT.get()).getDuration() > 200) {
                if (WorldSetup.ADHESION_BLOCK.get().coat(player.getLevel(), pos) > 0) {
                    EffectHelper.drainStormlight(player, 200);
                    Network.sendTo(new SurgePacket(Surge.ADHESION, pos, shiftHeld), player);
                }
            }
        }
    }


    public static void abrasion(ServerPlayerEntity player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (shiftHeld) {// Allow climbing
            EffectHelper.toggleEffect(player, PowersSetup.STICKING.get());
            player.removeEffect(PowersSetup.SLICKING.get());
        } else { // Allow sliding
            EffectHelper.toggleEffect(player, PowersSetup.SLICKING.get());
            player.removeEffect(PowersSetup.STICKING.get());
        }
    }


    public static void cohesion(ServerPlayerEntity player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (shiftHeld) {
            player.openMenu(
                    new SimpleNamedContainerProvider((i, inv, oplayer) -> new PortableStonecutterContainer(i, inv), new TranslationTextComponent("surge.cohesion.stoneshaping")));
        } else {
            EffectHelper.toggleEffect(player, PowersSetup.COHESION.get());
        }
    }


    public static void tension(ServerPlayerEntity player, @Nullable BlockPos pos, boolean shiftHeld) {
        // todo shift held - haste?
        EffectHelper.toggleEffect(player, PowersSetup.TENSION.get());
    }


    public static void division(ServerPlayerEntity player, @Nullable BlockPos pos, boolean shiftHeld) {
        // TODO config to disable breaking, give haste instead
        if (isBlockSafe(pos, player.level)) {
            if (EffectHelper.drainStormlight(player, 400)) {
                player.getLevel().explode(player, DamageSource.MAGIC, null, pos.getX(), pos.getY(), pos.getZ(), 1.5F, true, shiftHeld ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
            }
        }
    }


    public static void gravitation(ServerPlayerEntity player, @Nullable BlockPos l, boolean shiftHeld) {
        if (player.isOnGround() && shiftHeld) {
            // reverse lashing
            double range = 10;
            Vector3d pvec = player.position().add(0D, player.getEyeHeight() / 2.0, 0D);
            List<ItemEntity> items = player.level.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(pvec.subtract(range, range, range), pvec.add(range, range, range)));
            for (ItemEntity e : items) {
                Vector3d vec = pvec.subtract(e.position());
                e.setDeltaMovement(e.getDeltaMovement().add(vec.normalize().scale(vec.lengthSqr() * 0.1D)));
            }

        } else { // Basic lashing
            // TODO look into ENTITY_GRAVITY
            //            if (!shiftHeld) {
            //                EffectHelper.increasePermanentEffect(player, PowersSetup.GRAVITATION.get(), 6);
            //            } else {
            //                EffectHelper.decreasePermanentEffect(player, PowersSetup.GRAVITATION.get());
            //            }
            if (player.xRot < -70) {
                if (player.isNoGravity() || player.isOnGround()) {
                    player.setNoGravity(true);
                    player.setDeltaMovement(player.getDeltaMovement().add(0D, 0.5, 0D));
                    player.hurtMarked = true;

                } else {
                    player.setNoGravity(true);
                    player.setDeltaMovement(player.getDeltaMovement().multiply(1D, 0D, 1D));
                    player.hurtMarked = true;

                }
            } else if (player.xRot > 70) {
                if (player.isNoGravity()) {
                    if (player.getDeltaMovement().y > 0.1) {
                        player.setDeltaMovement(player.getDeltaMovement().multiply(1D, 0D, 1D));
                        player.hurtMarked = true;
                    } else {
                        player.setNoGravity(false);
                    }
                }

            } else {
                double facing = Math.toRadians(MathHelper.wrapDegrees(player.yHeadRot));
                player.setDeltaMovement(player.getDeltaMovement().add(-Math.sin(facing), 0, Math.cos(facing)));
                player.hurtMarked = true;
            }
        }
    }


    public static void illumination(ServerPlayerEntity player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (pos != null && player.level.isLoaded(pos)) {
            if (shiftHeld) {
                if (EffectHelper.drainStormlight(player, 300)) {
                    player.addEffect(new EffectInstance(Effects.INVISIBILITY, 600, 0, true, false, true));
                }
            } else { // Spawn ghost blocks
                if (player.getMainHandItem().getItem() instanceof BlockItem) {
                    // Allow rudimentary 'building'
                    if (!player.level.getBlockState(pos).isAir()) {
                        pos = pos.relative(Direction.orderedByNearest(player)[0].getOpposite());
                    }

                    while (!player.level.getEntitiesOfClass(FallingBlockEntity.class, new AxisAlignedBB(pos)).isEmpty()) {
                        pos = pos.relative(Direction.orderedByNearest(player)[0].getOpposite());
                    }

                    if (!player.level.getBlockState(pos).isAir()) {
                        return;
                    }

                    if (EffectHelper.drainStormlight(player, 20)) {
                        FallingBlockEntity entity = new FallingBlockEntity(player.getLevel(), pos.getX() + .5, pos.getY() - 0.009, pos.getZ() + .5,
                                                                           ((BlockItem) player.getMainHandItem().getItem()).getBlock().defaultBlockState());
                        entity.dropItem = false;
                        entity.setNoGravity(true);
                        entity.noPhysics = true;
                        entity.time = -1200;
                        player.getLevel().addFreshEntity(entity);
                    }
                } else if (player.getMainHandItem().isEmpty()) {
                    // remove entity if there
                    List<FallingBlockEntity> fallings = player.level.getEntitiesOfClass(FallingBlockEntity.class, new AxisAlignedBB(pos));
                    if (!fallings.isEmpty()) {
                        fallings.forEach(Entity::kill);
                    }
                }
            }
        }
    }


    public static void progression(ServerPlayerEntity player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (shiftHeld) { // Regen
            if (EffectHelper.drainStormlight(player, 600)) {
                player.addEffect(new EffectInstance(Effects.REGENERATION, 100, 4, true, false, true));
            }
        } else { // Growth
            if (isBlockSafe(pos, player.level)) {
                BlockState state = player.level.getBlockState(pos);
                if (state.getBlock() instanceof IGrowable) {
                    IGrowable igrowable = (IGrowable) state.getBlock();
                    if (igrowable.isValidBonemealTarget(player.level, pos, state, player.level.isClientSide)) {
                        if (igrowable.isBonemealSuccess(player.level, player.level.random, pos, state)) {
                            if (EffectHelper.drainStormlight(player, 100)) {
                                igrowable.performBonemeal(player.getLevel(), player.level.random, pos, state);
                                Network.sendTo(new SurgePacket(Surge.PROGRESSION, pos, shiftHeld), player);

                            }

                        }
                    }
                }
            }
        }
    }


    public static void transformation(ServerPlayerEntity player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (shiftHeld) {
            player.openMenu(
                    new SimpleNamedContainerProvider((i, inv, oplayer) -> new PortableCraftingContainer(i, inv), new TranslationTextComponent("surge.cohesion.soulcasting")));
        } else {
            if (isBlockSafe(pos, player.level)) {
                Block block = player.level.getBlockState(pos).getBlock();
                if (transformableBlocks.containsKey(block) && EffectHelper.drainStormlight(player, 600)) {
                    Block newBlock = transformableBlocks.get(block);
                    player.level.setBlockAndUpdate(pos, newBlock.defaultBlockState());
                }
            }
        }
    }


    public static void transportation(ServerPlayerEntity player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (shiftHeld) {
            if (EffectHelper.drainStormlight(player, 1200)) {
                // similar to allomancy
                double x, y, z;
                BlockPos respawnPosition = player.getRespawnPosition();
                RegistryKey<World> dimension = player.getRespawnDimension();
                if (respawnPosition == null) {
                    IWorldInfo info = player.level.getLevelData();
                    x = info.getXSpawn();
                    y = info.getYSpawn();
                    z = info.getZSpawn();
                    dimension = World.OVERWORLD;
                } else {
                    x = respawnPosition.getX() + 0.5;
                    y = respawnPosition.getY();
                    z = respawnPosition.getZ() + 0.5;
                }
                if (!dimension.equals(player.level.dimension())) {
                    player.changeDimension(player.getLevel().getServer().getLevel(player.getRespawnDimension()), new ITeleporter() {
                        @Override
                        public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                            Entity repositionedEntity = repositionEntity.apply(false);
                            repositionedEntity.teleportTo(x, y, z);
                            return repositionedEntity;
                        }
                    });
                }
                player.teleportToWithTicket(x, y + 1.5, z);
            }
        } else {
            if (isBlockSafe(pos, player.level)) {
                pos = findAdjacentBlock(pos, player);
                if (pos != null && EffectHelper.drainStormlight(player, 100)) {
                    player.teleportToWithTicket(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                }
            }
        }
    }
}
