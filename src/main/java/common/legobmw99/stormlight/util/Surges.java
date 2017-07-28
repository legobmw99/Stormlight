package common.legobmw99.stormlight.util;

import java.util.ArrayList;
import java.util.List;

import common.legobmw99.stormlight.network.packets.EffectEntityPacket;
import common.legobmw99.stormlight.network.packets.MoveEntityPacket;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Surges {

	private static List<String> transformableIn = buildInList();
	private static List<String> transformableOut = buildOutList();

	private static List<String> buildOutList() {
		List<String> list = new ArrayList<String>();
		list.add(Blocks.STONE.getRegistryName().toString());
		list.add(Blocks.RED_SANDSTONE.getRegistryName().toString());
		list.add(Blocks.MYCELIUM.getRegistryName().toString());
		list.add(Blocks.LAVA.getRegistryName().toString());
		list.add(Blocks.HAY_BLOCK.getRegistryName().toString());
		return list;
	}

	private static List<String> buildInList() {
		List<String> list = new ArrayList<String>();
		list.add(Blocks.COBBLESTONE.getRegistryName().toString());
		list.add(Blocks.SANDSTONE.getRegistryName().toString());
		list.add(Blocks.GRASS.getRegistryName().toString());
		list.add(Blocks.OBSIDIAN.getRegistryName().toString());
		list.add(Blocks.STONE.getRegistryName().toString());
		return list;
	}

	int used = 0;

	public void gravitation(EntityPlayerMP player, boolean shiftHeld) {
		if (shiftHeld) {
			// reverse lashing
			double range = 10;
			double x = player.posX;
			double y = player.posY + 1.5;
			double z = player.posZ;
			double factor = 9;
			List<EntityItem> items = player.world.getEntitiesWithinAABB(EntityItem.class,
					new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
			for (EntityItem e : items) {
				e.addVelocity((x - e.posX) / factor, (y - e.posY) / factor, (z - e.posZ) / factor);
				Registry.network.sendToServer(new MoveEntityPacket((x - e.posX) / factor, (y - e.posY) / factor,
						(z - e.posZ) / factor, e.getEntityId()));

			}

		} else { // Basic lashing
			if (player.rotationPitch < -80) {
				// up
				Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/flip.json"));
				Minecraft.getMinecraft().gameSettings.invertMouse = true;
				Registry.network.sendToServer(
						new EffectEntityPacket(25, 25000, 24, Minecraft.getMinecraft().player.getEntityId()));
				used = 0;
			} else {
				if (player.rotationPitch > 80) {
					// down
					Minecraft.getMinecraft().entityRenderer.stopUseShader();
					Minecraft.getMinecraft().gameSettings.invertMouse = false;
					if (player.isPotionActive(Potion.getPotionById(25))) {
						if (used == 0) {
							Registry.network.sendToServer(new EffectEntityPacket(25, 25000, -1,
									Minecraft.getMinecraft().player.getEntityId()));
							used = 1;
						} else {
							Registry.network.sendToServer(
									new EffectEntityPacket(25, 1, 0, Minecraft.getMinecraft().player.getEntityId()));
							used = 0;
						}
					}
				} else {
					Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
					EnumFacing enumfacing = entity.getHorizontalFacing();
					switch (enumfacing) {
					case SOUTH:
						// toward positive z
						player.motionZ = 5;
						Registry.network.sendToServer(new MoveEntityPacket(0, 0, 5, player.getEntityId()));
						break;
					case WEST:
						// toward negative x
						player.motionX = -5;
						Registry.network.sendToServer(new MoveEntityPacket(-5, 0, 0, player.getEntityId()));
						break;
					case NORTH:
						// toward negative z
						player.motionZ = -5;
						Registry.network.sendToServer(new MoveEntityPacket(0, 0, -5, player.getEntityId()));
						break;
					case EAST:
						// toward positive x
						player.motionX = 5;
						Registry.network.sendToServer(new MoveEntityPacket(5, 0, 0, player.getEntityId()));
						break;
					default:
						break;

					}
				}
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
						player.world.getHeight(player.world.getSpawnPoint()).getY(), player.world.getSpawnPoint().getZ(), player.cameraYaw,
						player.cameraPitch);
			}
		} else {
			EntityEnderPearl entityenderpearl = new EntityEnderPearl(player.world, player);
			entityenderpearl.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.0F, 0.0F);
			player.getEntityWorld().spawnEntity(entityenderpearl);
		}
	}

	private static boolean isBlockSafe(World world, BlockPos pos) {
		return world.isBlockLoaded(pos) && world.getBlockState(pos).getBlock() != Blocks.AIR;
	}

	public static void transformation(World world, BlockPos pos) {
		if (isBlockSafe(world, pos)) {
			Block block = world.getBlockState(pos).getBlock();
			if (transformableIn.contains(block.getRegistryName().toString())) {
				Block newBlock = Block.getBlockFromName(
						transformableOut.get(transformableIn.indexOf(block.getRegistryName().toString())));
				world.setBlockState(pos, newBlock.getDefaultState());
			}
		}
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

}
