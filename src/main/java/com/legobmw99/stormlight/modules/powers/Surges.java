package com.legobmw99.stormlight.modules.powers;

import com.legobmw99.stormlight.modules.powers.container.PortableCraftingContainer;
import com.legobmw99.stormlight.modules.powers.container.PortableStonecutterContainer;
import com.legobmw99.stormlight.modules.powers.effect.EffectHelper;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Surges {

    private static boolean isBlockSafe(BlockPos pos, World level) {
        return pos != null && level.isLoaded(pos) && !level.getBlockState(pos).isAir();
    }

    public static void test(ServerPlayerEntity player, @Nullable BlockPos looking, boolean modified) {

    }

    /* private static final Map<Block,Block> transformableBlocks = buildBlockMap();
    private static final Map<Item,Item> transformableItems = buildItemMap();

    private static Map<Block,Block>  buildBlockMap() {
        Map<Block,Block> map = new HashMap<Block,Block>();
        map.put(Blocks.COBBLESTONE, Blocks.STONE);
        map.put(Blocks.SANDSTONE,Blocks.RED_SANDSTONE);
        map.put(Blocks.GRASS, Blocks.MYCELIUM);
        map.put(Blocks.OBSIDIAN, Blocks.LAVA);
        map.put(Blocks.STONE, Blocks.HAY_BLOCK);
        map.put(Blocks.MELON, Blocks.PUMPKIN);
        map.put(Blocks.PUMPKIN, Blocks.MELON);
        map.put(Blocks.WHITE_WOOL, Blocks.COBWEB);

        return map;

    }

    private static Map<Item,Item> buildItemMap() {
        Map<Item,Item> map = new HashMap<Item,Item>();
        map.put(Items.GRAVEL, Items.FLINT);
        return map;
    }



*/
    public static void adhesion(ServerPlayerEntity player, BlockPos pos, boolean shiftHeld) {
        // todo shift held option?
        if (isBlockSafe(pos, player.level)) {
            WorldSetup.ADHESION_BLOCK.get().coat(player.getLevel(), pos);
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

    public static void division(ServerPlayerEntity player, @Nullable BlockPos pos, boolean shiftHeld) {
        // TODO config to disable breaking
        if (isBlockSafe(pos, player.level)) {
            player.getLevel().explode(player, DamageSource.MAGIC, null, pos.getX(), pos.getY(), pos.getZ(), 1.5F, true, shiftHeld ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
        }
    }

    public static void gravitation(ServerPlayerEntity player, @Nullable BlockPos l, boolean shiftHeld) {
        if (shiftHeld) {
            // reverse lashing
            double range = 10;
            Vector3d pvec = player.position().add(0D, player.getEyeHeight() / 2.0, 0D);
            List<ItemEntity> items = player.level.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(pvec.subtract(range, range, range), pvec.add(range, range, range)));
            for (ItemEntity e : items) {
                Vector3d vec = pvec.subtract(e.position());
                e.setDeltaMovement(e.getDeltaMovement().add(vec.normalize().scale(vec.lengthSqr() * 0.1D)));
            }

        } else { // Basic lashing

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
                player.addEffect(new EffectInstance(Effects.INVISIBILITY, 600, 0, true, true));
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


                    FallingBlockEntity entity = new FallingBlockEntity(player.getLevel(), pos.getX() + .5, pos.getY() - 0.009, pos.getZ() + .5,
                                                                       ((BlockItem) player.getMainHandItem().getItem()).getBlock().defaultBlockState());
                    entity.dropItem = false;
                    entity.setNoGravity(true);
                    entity.noPhysics = true;
                    entity.time = -1200;
                    player.getLevel().addFreshEntity(entity);

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
            player.addEffect(new EffectInstance(Effects.REGENERATION, 100, 4, true, true));
        } else { // Growth
            if (isBlockSafe(pos, player.level)) {
                BlockState state = player.level.getBlockState(pos);
                if (state.getBlock() instanceof IGrowable) {
                    IGrowable igrowable = (IGrowable) state.getBlock();
                    if (igrowable.isValidBonemealTarget(player.level, pos, state, player.level.isClientSide)) {
                        if (igrowable.isBonemealSuccess(player.level, player.level.random, pos, state)) {
                            igrowable.performBonemeal(player.getLevel(), player.level.random, pos, state);
                            // Should spawn bonemeal particles
                            player.getLevel().levelEvent(player, 2005, pos, 0);
                        }
                    }
                }
            }
        }
    }

    /*

    private static void switchItemInMainhand(EntityPlayerMP player, Item toItem){
        ItemStack toItemStack = new ItemStack(toItem,player.getHeldItemMainhand().getCount());
        player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.AIR, 0));
        player.inventory.setInventorySlotContents(player.inventory.currentItem, toItemStack);
    }

    public static void tension(World entityWorld, BlockPos pos, boolean shiftHeld) {

    }

*/
    public static void transformation(ServerPlayerEntity player, @Nullable BlockPos pos, boolean shiftHeld) {
        if (shiftHeld) {
            player.openMenu(
                    new SimpleNamedContainerProvider((i, inv, oplayer) -> new PortableCraftingContainer(i, inv), new TranslationTextComponent("surge.cohesion.soulcasting")));
        } else {
            if (isBlockSafe(pos, player.level)) {
                // Block block = player.level.getBlockState(pos).getBlock();
                //                if (transformableBlocks.containsKey(block)) {
                //                    Block newBlock = transformableBlocks.get(block);
                //                    player.world.setBlockState(pos, newBlock.getDefaultState());
                //                }
            }
        }
    }

	/*
	public static void transportation(EntityPlayerMP player, @Nullable EntitySpren spren, boolean shiftHeld) {
		if (shiftHeld) {
			if (player.dimension != 0) {
				player.changeDimension(0);
			}
			if (player.getBedLocation() != null
					&& player.getBedSpawnLocation(player.world, player.getBedLocation(), false) != null) {
				player.connection.setPlayerLocation(
						player.getBedSpawnLocation(player.world, player.getBedLocation(), false).getX(),
						player.getBedSpawnLocation(player.world, player.getBedLocation(), false).getY(),
						player.getBedSpawnLocation(player.world, player.getBedLocation(), false).getZ(),
						player.cameraYaw, player.cameraPitch);
			} else {
				player.connection.setPlayerLocation(player.world.getSpawnPoint().getX(),
						player.world.getHeight(player.world.getSpawnPoint()).getY(),
						player.world.getSpawnPoint().getZ(), player.cameraYaw, player.cameraPitch);
			}
		} else {
			EntityEnderPearl entityenderpearl = new EntityEnderPearl(player.world, player);
			entityenderpearl.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.0F, 0.0F);
			player.getEntityWorld().spawnEntity(entityenderpearl);
		}
	} */
}
