package com.legobmw99.stormlight.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.List;

public enum Surge {
    ADHESION(Surges::test),
    GRAVITATION(Surges::gravitation),
    DIVISION(Surges::test),
    ABRASION(Surges::test),
    PROGRESSION(Surges::test),
    ILLUMINATION(Surges::illumination),
    TRANSFORMATION(Surges::test),
    TRANSPORTATION(Surges::test),
    COHESION(Surges::test),
    TENSION(Surges::test);

    private final ISurge surge;

    Surge(ISurge surge) {
        this.surge = surge;
    }

    public void fire(ServerPlayerEntity player, @Nullable BlockPos looking, boolean modified) {
        surge.call(player, looking, modified);
    }

}

@FunctionalInterface
interface ISurge {
    void call(ServerPlayerEntity player, @Nullable BlockPos looking, boolean modified);
}

class Surges {

    static void test(ServerPlayerEntity player, @Nullable BlockPos looking, boolean modified) {

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

	public static void abrasion(EntityPlayerMP player, boolean shiftHeld) {
		if (shiftHeld) {// Allow slipping
			// This is the best we can really do
			player.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 600, 2, true, false));
		} else { // Allow climbing
			// Use the same method in EntityLivingBase.move() to determine if a player is near a block.
			if (!player.getEntityWorld().getCollisionBoxes(player, player.getEntityBoundingBox().offset(0.15, 0, 0.15)).isEmpty()
					|| !player.getEntityWorld().getCollisionBoxes(player, player.getEntityBoundingBox().offset(-0.15, 0, -0.15)).isEmpty()) {
				player.motionY = 0.2D;
				player.velocityChanged = true;
			}
		}
	}

	public static void adhesion(World world, BlockPos pos, boolean shiftHeld) {

	}

	public static void cohesion(EntityPlayerMP player, BlockPos pos, boolean shiftHeld) {
		player.addPotionEffect(new PotionEffect(Potion.getPotionById(3), 600, 1, false, true));

	}

	public static void division(EntityPlayerMP player, BlockPos pos, boolean shiftHeld) {
		// TODO config to disable this
		if (isBlockSafe(player.getEntityWorld(), pos)) {
			player.getEntityWorld().newExplosion(player, pos.getX(), pos.getY(), pos.getZ(), 1.5F, true, true);
		}
	}*/

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
                if (player.isNoGravity()) {
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


    public static void illumination(ServerPlayerEntity player, BlockPos pos, boolean shiftHeld) {
        if (player.level.isLoaded(pos)) {
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

	/*
	private static boolean isBlockSafe(World world, BlockPos pos) {
		return world.isBlockLoaded(pos) && world.getBlockState(pos).getBlock() != Blocks.AIR;
	}

	public static void progression(EntityPlayerMP player, BlockPos pos, boolean shiftHeld) {
		if (shiftHeld) { // Regen
			player.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 100, 4, true, true));
		} else { // Growth
			if (isBlockSafe(player.getEntityWorld(), pos)) {
				IBlockState ibs = player.getEntityWorld().getBlockState(pos);
				if (ibs.getBlock() instanceof IGrowable) {
					IGrowable igrowable = (IGrowable) ibs.getBlock();
					if (igrowable.canGrow(player.world, pos, ibs, player.world.isRemote)) {
						if (igrowable.canUseBonemeal(player.world, player.world.rand, pos, ibs)) {
							igrowable.grow(player.world, player.world.rand, pos, ibs);
							// Should spawn bonemeal particles
							player.getEntityWorld().playEvent(player, 2005, pos, 15);
						}
					}
				}
			}
		}
	}

	private static void switchItemInMainhand(EntityPlayerMP player, Item toItem){
		ItemStack toItemStack = new ItemStack(toItem,player.getHeldItemMainhand().getCount());
		player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.AIR, 0));
		player.inventory.setInventorySlotContents(player.inventory.currentItem, toItemStack);
	}

	public static void tension(World entityWorld, BlockPos pos, boolean shiftHeld) {

	}

	public static void transformation(EntityPlayerMP player, BlockPos pos, boolean shiftHeld) {
		if(shiftHeld){
			if (isBlockSafe(player.world, pos)) {
				Block block = player.world.getBlockState(pos).getBlock();
				if (transformableBlocks.containsKey(block)) {
					Block newBlock = transformableBlocks.get(block);
					player.world.setBlockState(pos, newBlock.getDefaultState());
				}
			}
		} else {
			Item item = player.getHeldItemMainhand().getItem();
			if(transformableItems.containsKey(item)){

			}else if(item == Items.STICK){
				if(player.world.rand.nextInt(100) == 0) {
					switchItemInMainhand(player, Items.FIRE_CHARGE);
					player.sendMessage(new TextComponentString("<Stick> I am.... fire"));
				} else {
					player.sendMessage(new TextComponentString("<Stick> I am a stick"));
				}
			}
		}
	}

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