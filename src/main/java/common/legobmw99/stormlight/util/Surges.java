package common.legobmw99.stormlight.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import common.legobmw99.stormlight.entity.EntitySpren;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class Surges {

	private static final Map<Block,Block> transformableBlocks = buildBlockMap();
	private static final Map<Item,Item> transformableItems = buildItemMap();

	private static Map<Block,Block>  buildBlockMap() {
		Map<Block,Block> map = new HashMap<Block,Block>();
		map.put(Blocks.COBBLESTONE, Blocks.STONE);
		map.put(Blocks.SANDSTONE,Blocks.RED_SANDSTONE);
		map.put(Blocks.GRASS, Blocks.MYCELIUM);
		map.put(Blocks.OBSIDIAN, Blocks.LAVA);
		map.put(Blocks.STONE, Blocks.HAY_BLOCK);
		map.put(Blocks.MELON_BLOCK, Blocks.PUMPKIN);
		map.put(Blocks.PUMPKIN, Blocks.MELON_BLOCK);
		map.put(Blocks.WOOL, Blocks.WEB);
		
		return map;
		
	}

	private static Map<Item,Item> buildItemMap() {
		Map<Item,Item> map = new HashMap<Item,Item>();
		map.put(ItemBlock.getItemFromBlock(Blocks.GRAVEL), Items.FLINT);
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

				player.motionZ = MathHelper.clamp(player.motionZ, -2.5, 2.5);
				player.motionX = MathHelper.clamp(player.motionX, -2.5, 2.5);

				player.velocityChanged = true;

			}
		}
	}

	public static void illumination(EntityPlayerMP player, BlockPos pos, boolean shiftHeld) {
		if (player.getEntityWorld().isBlockLoaded(pos)) {
			if (shiftHeld) {
				player.addPotionEffect(new PotionEffect(Potion.getPotionById(14), 600, 0, true, false));
			} else { // Spawn ghost blocks
				if (player.getHeldItemMainhand().getItem() instanceof ItemBlock) {
					// Allow rudimentary 'building'
					while (!player.world.getEntitiesWithinAABB(EntityFallingBlock.class, new AxisAlignedBB(pos))
							.isEmpty()) {
						pos = pos.up();
					}
					EntityFallingBlock entity = new EntityFallingBlock(player.getEntityWorld(), pos.getX() + .5,
							pos.getY() - 0.010, pos.getZ() + .5,
							((ItemBlock) player.getHeldItemMainhand().getItem()).getBlock().getDefaultState());
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
	}

}
