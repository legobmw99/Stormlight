package common.legobmw99.stormlight.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class Surges {

	private static List<String> transformableIn = buildInList();
	private static List<String> transformableOut = buildOutList();

	private static List<String> buildInList() {
		List<String> list = new ArrayList<String>();
		list.add(Blocks.COBBLESTONE.getRegistryName().toString());
		list.add(Blocks.SANDSTONE.getRegistryName().toString());
		list.add(Blocks.GRASS.getRegistryName().toString());
		list.add(Blocks.OBSIDIAN.getRegistryName().toString());
		list.add(Blocks.STONE.getRegistryName().toString());
		list.add(Blocks.MELON_BLOCK.getRegistryName().toString());
		list.add(Blocks.PUMPKIN.getRegistryName().toString());

		return list;
	}

	private static List<String> buildOutList() {
		List<String> list = new ArrayList<String>();
		list.add(Blocks.STONE.getRegistryName().toString());
		list.add(Blocks.RED_SANDSTONE.getRegistryName().toString());
		list.add(Blocks.MYCELIUM.getRegistryName().toString());
		list.add(Blocks.LAVA.getRegistryName().toString());
		list.add(Blocks.HAY_BLOCK.getRegistryName().toString());
		list.add(Blocks.PUMPKIN.getRegistryName().toString());
		list.add(Blocks.MELON_BLOCK.getRegistryName().toString());
		return list;
	}

	
	
	public static void abrasion(EntityPlayerMP player, boolean shiftHeld) {
		
	}

	public static void adhesion(World world, BlockPos pos, boolean shiftHeld) {
		
	}

	public static void cohesion(World entityWorld, BlockPos pos, boolean shiftHeld) {
		
	}

	public static void division(EntityPlayerMP player, BlockPos pos, boolean shiftHeld) {
		// TODO config to disable this
		if (isBlockSafe(player.getEntityWorld(), pos)) {
			player.getEntityWorld().newExplosion(player, pos.getX(), pos.getY(), pos.getZ(), 1.5F, true, true);
		}
	}

	public static void gravitation(EntityPlayerMP player, boolean shiftHeld) {
		if (shiftHeld) {
			// reverse lashing
			double range = 10;
			double x = player.posX;
			double y = player.posY + 1.5;
			double z = player.posZ;
			double factor = 9;
			List<EntityItem> items = player.world.getEntitiesWithinAABB(EntityItem.class,
					new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
			for (Entity e : items) {
				if (e != player) {
					e.motionX = (x - e.posX) / factor;
					e.motionY = (y - e.posY) / factor;
					e.motionZ = (z - e.posZ) / factor;
					e.velocityChanged = true;
				}
			}

		} else { // Basic lashing

			if (player.rotationPitch < -70) {
				if (player.hasNoGravity()) {
					player.motionY += 0.5;
					player.motionY = MathHelper.clamp(player.motionY, 0, 2.0);
					player.velocityChanged = true;
				} else {
					player.setNoGravity(true);
					player.motionY = 0;
					player.velocityChanged = true;
				}
			} else if (player.rotationPitch > 70) {
				if (player.hasNoGravity()) {
					player.setNoGravity(false);
				}

			} else {
				double facing = Math.toRadians(MathHelper.wrapDegrees(player.rotationYawHead));
				player.motionZ += 1 * Math.cos(facing);
				player.motionX += -1 * Math.sin(facing);

				player.motionZ = MathHelper.clamp(player.motionZ, -5, 5);
				player.motionX = MathHelper.clamp(player.motionX, -5, 5);

				player.velocityChanged = true;

			}
		}
	}

	public static void illumination(EntityPlayerMP player, BlockPos pos, boolean shiftHeld) {
		if(player.getEntityWorld().isBlockLoaded(pos)){
			if(shiftHeld){
				player.addPotionEffect(new PotionEffect(Potion.getPotionById(14), 1200, 0, true, false));
			} else { //Spawn ghost blocks
				if(player.getHeldItemMainhand().getItem() instanceof ItemBlock){
					//Allow rudimentary 'building'
					while(!player.world.getEntitiesWithinAABB(EntityFallingBlock.class, new AxisAlignedBB(pos)).isEmpty()){
						pos = pos.up();
					}
					EntityFallingBlock entity = new EntityFallingBlock(player.getEntityWorld(), pos.getX() + .5, pos.getY(),pos.getZ() +.5,((ItemBlock)player.getHeldItemMainhand().getItem()).getBlock().getDefaultState());
					entity.setNoGravity(true);
					entity.setEntityInvulnerable(true);
					entity.fallTime = -500;
					player.getEntityWorld().spawnEntity(entity);
				}
			}
		}
	}

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
						}
					}
				}
			}
		}
	}

	public static void tension(World entityWorld, BlockPos pos, boolean shiftHeld) {

	}

	public static void transformation(World world, BlockPos pos, boolean shiftHeld) {
		if (isBlockSafe(world, pos)) {
			Block block = world.getBlockState(pos).getBlock();
			if (transformableIn.contains(block.getRegistryName().toString())) {
				Block newBlock = Block.getBlockFromName(
						transformableOut.get(transformableIn.indexOf(block.getRegistryName().toString())));
				world.setBlockState(pos, newBlock.getDefaultState());
			}
		}
	}

	public static void transportation(EntityPlayerMP player, boolean shiftHeld) {
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
	}

}
