package common.legobmw99.stormlight.util;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import common.legobmw99.stormlight.network.packets.EffectEntityPacket;
import common.legobmw99.stormlight.network.packets.GrowPacket;
import common.legobmw99.stormlight.network.packets.MoveEntityPacket;
import common.legobmw99.stormlight.network.packets.TeleportPlayerPacket;
import common.legobmw99.stormlight.network.packets.TransformBlockPacket;

public class Surges {
	private LinkedList<Integer>  transformableIn;
	private LinkedList<Integer>  transformableOut;
	

	public Surges(){
		this.buildLists();
	}
	

	private void buildLists() {
		this.transformableIn = new LinkedList<Integer>();
		this.transformableOut = new LinkedList<Integer>();

		this.transformableIn.add(Blocks.COBBLESTONE.getStateId(Blocks.COBBLESTONE.getDefaultState()));
		this.transformableOut.add(Blocks.STONE.getStateId(Blocks.STONE.getDefaultState()));
		
		this.transformableIn.add(Blocks.SANDSTONE.getStateId(Blocks.SANDSTONE.getDefaultState()));
		this.transformableOut.add(Blocks.RED_SANDSTONE.getStateId(Blocks.RED_SANDSTONE.getDefaultState()));
		
		this.transformableIn.add(Blocks.GRASS.getStateId(Blocks.GRASS.getDefaultState()));
		this.transformableOut.add(Blocks.MYCELIUM.getStateId(Blocks.MYCELIUM.getDefaultState()));
		
		this.transformableIn.add(Blocks.OBSIDIAN.getStateId(Blocks.OBSIDIAN.getDefaultState()));
		this.transformableOut.add(Blocks.LAVA.getStateId(Blocks.LAVA.getDefaultState()));
		
	}


	int used = 0;
	public void gravitation(EntityPlayer player, int i){
		//Basic lashing
		if(i == 0){
			if(player.rotationPitch < -80){
				//up
				Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/flip.json"));
				Minecraft.getMinecraft().gameSettings.invertMouse = true;
				Registry.network.sendToServer(new EffectEntityPacket(25,25000, 24, Minecraft.getMinecraft().thePlayer.getEntityId()));
				used = 0;
			} else {
				if(player.rotationPitch > 80){
					//down
					Minecraft.getMinecraft().entityRenderer.stopUseShader();
					Minecraft.getMinecraft().gameSettings.invertMouse = false;
					if(player.isPotionActive(Potion.getPotionById(25))){
						if (used == 0){
							Registry.network.sendToServer(new EffectEntityPacket(25,25000, -1, Minecraft.getMinecraft().thePlayer.getEntityId()));
							used = 1;
						}  else {
							Registry.network.sendToServer(new EffectEntityPacket(25,1, 0, Minecraft.getMinecraft().thePlayer.getEntityId()));
							used = 0;
						}
					}
				} else {
					Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
					EnumFacing enumfacing = entity.getHorizontalFacing();
					switch (enumfacing){
					case SOUTH:
						//toward positive z
						player.motionZ = 5;
						Registry.network.sendToServer(new MoveEntityPacket(0,0,5,player.getEntityId()));
						break;
					case WEST:
						//toward negative x
						player.motionX = -5;
						Registry.network.sendToServer(new MoveEntityPacket(-5,0,0,player.getEntityId()));
						break;
					case NORTH:
						//toward negative z
						player.motionZ = -5;
						Registry.network.sendToServer(new MoveEntityPacket(0,0,-5,player.getEntityId()));
						break;
					case EAST:
						//toward positive x
						player.motionX = 5;
						Registry.network.sendToServer(new MoveEntityPacket(5,0,0,player.getEntityId()));
						break;
					default:
						break;

					}
				}
			}
		} else {
			//reverse lashing
			if(i == 1){
				double range = 10;
				double x = player.posX;
				double y = player.posY + 1.5;
				double z = player.posZ;
				double factor = 9;
				List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
				for(EntityItem e: items)
				{
					e.addVelocity((x - e.posX) / factor, (y - e.posY) / factor, (z - e.posZ) / factor);
					Registry.network.sendToServer(new MoveEntityPacket((x - e.posX) / factor, (y - e.posY) / factor, (z - e.posZ) / factor,e.getEntityId()));

				}
			}
		}
	}
	public void transportation(EntityPlayer player, int i){
		Registry.network.sendToServer(new TeleportPlayerPacket(player.getEntityId(), i));
	}
	
	public void transformation(EntityPlayer player) {
		//doesnt work with meta values it seems
		//basically has to go down to ids and back up 
		RayTraceResult ray;
		ray = player.rayTrace(20.0F, 0.0F);
		if(ray.typeOfHit == RayTraceResult.Type.BLOCK){
			IBlockState ibs = Minecraft.getMinecraft().theWorld.getBlockState(ray.getBlockPos());
			if(this.transformableIn.contains(ibs.getBlock().getStateId(ibs.getBlock().getDefaultState()))){
				Registry.network.sendToServer(new TransformBlockPacket(this.transformableOut.get(this.transformableIn.indexOf(ibs.getBlock().getStateId(ibs.getBlock().getDefaultState()))),ray.getBlockPos()));				
			}
		}
	}
	public void progression(EntityPlayer player, int i){
		//Growth
		if(i==0){
			RayTraceResult ray = player.rayTrace(20.0F, 0.0F);
			if(ray.typeOfHit == RayTraceResult.Type.BLOCK){
				IBlockState ibs = Minecraft.getMinecraft().theWorld.getBlockState(ray.getBlockPos());
				if (ibs.getBlock() instanceof IGrowable){
					IGrowable igrowable = (IGrowable)ibs.getBlock();
			        if (igrowable.canGrow(player.worldObj, ray.getBlockPos(), ibs, player.worldObj.isRemote)){
						Registry.network.sendToServer(new GrowPacket(ray.getBlockPos().getX(),ray.getBlockPos().getY(),ray.getBlockPos().getZ()));				
			        }
			    }
			}
		} else {
			if(i==1){
				Registry.network.sendToServer(new EffectEntityPacket(10,100, 4, Minecraft.getMinecraft().thePlayer.getEntityId()));

			}
		}
	}
	
	
}
