package com.legobmw99.stormlight.modules.powers;

import com.legobmw99.stormlight.modules.powers.container.PortableCraftingContainer;
import com.legobmw99.stormlight.modules.powers.container.PortableStonecutterContainer;
import com.legobmw99.stormlight.modules.powers.effect.EffectHelper;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.network.Network;
import com.legobmw99.stormlight.network.packets.SurgePacket;
import com.legobmw99.stormlight.util.Surge;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Surges {

    private static final Direction[] DIRECTIONS = {Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private static final Map<Block, Block> transformableBlocks = buildBlockMap();

    private static boolean isBlockSafe(BlockPos pos, Level level) {
        return pos != null && level.isLoaded(pos) && !level.getBlockState(pos).isAir();
    }

    private static BlockPos findAdjacentBlock(BlockPos pos, ServerPlayer player) {
        for (Direction d : DIRECTIONS) {
            BlockPos adj = pos.relative(d);
            if (!player.level.getBlockState(adj).isSuffocating(player.level, adj)) {
                return adj;
            }
        }
        return null;
    }

    private static Map<Block, Block> buildBlockMap() {
        Map<Block, Block> map = new HashMap<Block, Block>();
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

    public static void adhesion(ServerPlayer player, BlockPos pos, boolean shiftHeld) {
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


    public static void abrasion(ServerPlayer player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (shiftHeld) {// Allow climbing
            EffectHelper.toggleEffect(player, PowersSetup.STICKING.get());
            player.removeEffect(PowersSetup.SLICKING.get());
        } else { // Allow sliding
            EffectHelper.toggleEffect(player, PowersSetup.SLICKING.get());
            player.removeEffect(PowersSetup.STICKING.get());
        }
    }


    public static void cohesion(ServerPlayer player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (shiftHeld) {
            player.openMenu(new SimpleMenuProvider((i, inv, oplayer) -> new PortableStonecutterContainer(i, inv), Component.translatable("surge.cohesion.stoneshaping")));
        } else {
            EffectHelper.toggleEffect(player, PowersSetup.COHESION.get());
        }
    }


    public static void tension(ServerPlayer player, @Nullable BlockPos pos, boolean shiftHeld) {
        // todo shift held - haste?
        EffectHelper.toggleEffect(player, PowersSetup.TENSION.get());
    }


    public static void division(ServerPlayer player, @Nullable BlockPos pos, boolean shiftHeld) {
        // TODO config to disable breaking, give haste instead
        if (isBlockSafe(pos, player.level)) {
            if (EffectHelper.drainStormlight(player, 400)) {
                player
                        .getLevel()
                        .explode(player, player.level.damageSources().magic(), null, pos.getX(), pos.getY(), pos.getZ(), 1.5F, true,
                                 shiftHeld ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE);
            }
        }
    }


    public static void gravitation(ServerPlayer player, @Nullable BlockPos l, boolean shiftHeld) {
        if (player.isOnGround() && shiftHeld) {
            // reverse lashing
            double range = 10;
            Vec3 pvec = player.position().add(0D, player.getEyeHeight() / 2.0, 0D);
            List<ItemEntity> items = player.level.getEntitiesOfClass(ItemEntity.class, new AABB(pvec.subtract(range, range, range), pvec.add(range, range, range)));
            for (ItemEntity e : items) {
                Vec3 vec = pvec.subtract(e.position());
                e.setDeltaMovement(e.getDeltaMovement().add(vec.normalize().scale(vec.lengthSqr() * 0.1D)));
            }

        } else { // Basic lashing
            // TODO set nogravity, store a vector (? where) and accelerate in that direction.
            // accelerate based on effect level
            // will also let you yeet other entities?
            //   - if shift held and entity hit is a living entitiy?
            //   - mark entity (give effect and glowing)
            //   - on next click, set vector
            // TODO look into ENTITY_GRAVITY
            //            if (!shiftHeld) {
            //                EffectHelper.increasePermanentEffect(player, PowersSetup.GRAVITATION.get(), 6);
            //            } else {
            //                EffectHelper.decreasePermanentEffect(player, PowersSetup.GRAVITATION.get());
            //            }
            if (player.getXRot() < -70) {
                if (player.isNoGravity() || player.isOnGround()) {
                    player.setNoGravity(true);
                    player.setDeltaMovement(player.getDeltaMovement().add(0D, 0.5, 0D));
                    player.hurtMarked = true;

                } else {
                    player.setNoGravity(true);
                    player.setDeltaMovement(player.getDeltaMovement().multiply(1D, 0D, 1D));
                    player.hurtMarked = true;

                }
            } else if (player.getXRot() > 70) {
                if (player.isNoGravity()) {
                    if (player.getDeltaMovement().y > 0.1) {
                        player.setDeltaMovement(player.getDeltaMovement().multiply(1D, 0D, 1D));
                        player.hurtMarked = true;
                    } else {
                        player.setNoGravity(false);
                    }
                }

            } else {
                double facing = Math.toRadians(Mth.wrapDegrees(player.yHeadRot));
                player.setDeltaMovement(player.getDeltaMovement().add(-Math.sin(facing), 0, Math.cos(facing)));
                player.hurtMarked = true;
            }
        }
    }


    public static void illumination(ServerPlayer player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (pos != null && player.level.isLoaded(pos)) {
            if (shiftHeld) {
                if (EffectHelper.drainStormlight(player, 300)) {
                    player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 600, 0, true, false, true));
                }
            } else { // Spawn ghost blocks
                if (player.getMainHandItem().getItem() instanceof BlockItem) {
                    // Allow rudimentary 'building'
                    if (!player.level.getBlockState(pos).isAir()) {
                        pos = pos.relative(Direction.orderedByNearest(player)[0].getOpposite());
                    }

                    while (!player.level.getEntitiesOfClass(FallingBlockEntity.class, new AABB(pos)).isEmpty()) {
                        pos = pos.relative(Direction.orderedByNearest(player)[0].getOpposite());
                    }

                    if (!player.level.getBlockState(pos).isAir()) {
                        return;
                    }

                    if (EffectHelper.drainStormlight(player, 20)) {
                        // TODO consider using DisplayBlock?
                        FallingBlockEntity entity = FallingBlockEntity.fall(player.getLevel(), pos,
                                                                            ((BlockItem) player.getMainHandItem().getItem()).getBlock().defaultBlockState());
                        entity.dropItem = false;
                        entity.noPhysics = true;
                        entity.setNoGravity(true);
                        entity.noPhysics = true;
                        entity.time = -1200;
                        entity.setDeltaMovement(Vec3.ZERO);
                        entity.setPos(Vec3.atBottomCenterOf(pos));
                        entity.hurtMarked = true;

                    }
                } else if (player.getMainHandItem().isEmpty()) {
                    // remove entity if there
                    List<FallingBlockEntity> fallings = player.level.getEntitiesOfClass(FallingBlockEntity.class, new AABB(pos));
                    if (!fallings.isEmpty()) {
                        fallings.forEach(Entity::kill);
                    }
                }
            }
        }
    }


    public static void progression(ServerPlayer player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (shiftHeld) { // Regen
            if (EffectHelper.drainStormlight(player, 600)) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 4, true, false, true));
            }
        } else { // Growth
            if (isBlockSafe(pos, player.level)) {
                BlockState state = player.level.getBlockState(pos);
                if (state.getBlock() instanceof BonemealableBlock igrowable) {
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


    public static void transformation(ServerPlayer player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (shiftHeld) {
            player.openMenu(new SimpleMenuProvider((i, inv, oplayer) -> new PortableCraftingContainer(i, inv), Component.translatable("surge.cohesion.soulcasting")));
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


    public static void transportation(ServerPlayer player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (shiftHeld) {
            if (EffectHelper.drainStormlight(player, 1200)) {
                // similar to allomancy
                double x, y, z;
                BlockPos respawnPosition = player.getRespawnPosition();
                ResourceKey<Level> dimension = player.getRespawnDimension();
                if (respawnPosition == null) {
                    LevelData info = player.level.getLevelData();
                    x = info.getXSpawn();
                    y = info.getYSpawn();
                    z = info.getZSpawn();
                    dimension = Level.OVERWORLD;
                } else {
                    x = respawnPosition.getX() + 0.5;
                    y = respawnPosition.getY();
                    z = respawnPosition.getZ() + 0.5;
                }
                if (!dimension.equals(player.level.dimension())) {
                    player.changeDimension(player.getLevel().getServer().getLevel(player.getRespawnDimension()), new ITeleporter() {
                        @Override
                        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
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
