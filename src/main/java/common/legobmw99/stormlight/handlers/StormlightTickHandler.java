package common.legobmw99.stormlight.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import common.legobmw99.stormlight.network.packets.EffectEntityPacket;
import common.legobmw99.stormlight.network.packets.MoveEntityPacket;
import common.legobmw99.stormlight.network.packets.StopFallPacket;
import common.legobmw99.stormlight.util.Registry;

public class StormlightTickHandler {


	int used = 0;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		EntityPlayerSP player;
		player = Minecraft.getMinecraft().thePlayer;
		if(player != null){
			//Recall
			if(Registry.Recall.isPressed()){
				//needs to be run on server
/*				System.out.println("Working 1");
				if(player.getHeldItemMainhand() == (ItemStack)null){
					System.out.println("Working 2");
					InventoryEnderChest inv = player.getInventoryEnderChest();
					if(inv != null){
						System.out.println("Working 3");
						for(int i=0; i < inv.getSizeInventory(); i++){
							System.out.println(i);
							if (inv.getStackInSlot(i) != (ItemStack)null){ 
								System.out.println("Working 4");
								if(inv.getStackInSlot(i).isItemEqual(new ItemStack(Item.getByNameOrId("stormlight:honorblade.windrunners")))){
									System.out.println("Working 5");
									player.inventory.addItemStackToInventory(inv.getStackInSlot(i));
									inv.removeStackFromSlot(i);
									break;
								} else{
									continue;
								}
							}
						}
					}
				}*/
			}
			//Surges
			if(player.isPotionActive(Registry.effectStormlight)){
				//Windrunners
				if(player.inventory.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.windrunners")))){
					if(Registry.BindingOne.isPressed()){
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
					}
				}
				if(Registry.BindingTwo.isPressed()){

				}
			}
			if(Registry.Reset.isPressed()){
				Minecraft.getMinecraft().gameSettings.invertMouse = false;
				Minecraft.getMinecraft().entityRenderer.stopUseShader();
				Registry.network.sendToServer(new EffectEntityPacket(25,1, 0, Minecraft.getMinecraft().thePlayer.getEntityId()));

			}
		}
	}


	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event){
		WorldInfo info = event.world.getWorldInfo();
		if(info.getWorldTime() % 96000 == 0){
			info.setCleanWeatherTime(0);
			info.setRainTime(2400);
			info.setThunderTime(2400);
			info.setRaining(true);
			info.setThundering(true);

		}

	}
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event){
		if(event.getEntityLiving().isPotionActive(Registry.effectStormlight)){
			event.getEntityLiving().setGlowing(true);
			Registry.network.sendToServer(new StopFallPacket());

		}else{
			if(event.getEntityLiving().isPotionActive(Potion.getPotionById(25))){
				Registry.network.sendToServer(new EffectEntityPacket(25,0, 0, Minecraft.getMinecraft().thePlayer.getEntityId()));
			}
			event.getEntityLiving().setGlowing(false);
		}

	}

	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onItemToss(ItemTossEvent event){
		if(event.getEntity().getEntityWorld().isThundering()){
			if(event.getEntityItem()!= null){
				if(event.getEntityItem().getEntityItem().isItemEqual(new ItemStack(Registry.itemSphere))){
					double x,y,z;
					int a;
					x = event.getEntityItem().posX;
					y = event.getEntityItem().posY;
					z = event.getEntityItem().posZ;
					a = event.getEntityItem().getEntityItem().stackSize;
					EntityItem entity = new EntityItem(event.getEntity().getEntityWorld(), x, y, z, new ItemStack(Item.getByNameOrId("Stormlight:sphere.charged"), a, 0));
					if(event.getEntity().isEntityAlive()){
						event.getEntity().getEntityWorld().spawnEntityInWorld(entity);
						event.getEntity().setDead();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onDamage(LivingHurtEvent event) {
		//Increase outgoing damage for stormlit people
		if (event.getSource().getSourceOfDamage() instanceof EntityPlayerMP) {
			EntityPlayerMP source = (EntityPlayerMP) event.getSource().getSourceOfDamage();
			if (source.isPotionActive(Registry.effectStormlight)) {
				event.setAmount(event.getAmount() + 2);
			}
		}
		//Reduce incoming damage for stormlit people
		if (event.getEntityLiving() instanceof EntityPlayerMP) {
			EntityPlayerMP source = (EntityPlayerMP) event.getEntityLiving();
			if (source.isPotionActive(Registry.effectStormlight)) {
				event.setAmount(event.getAmount() - 2);

			}
		}
	}
}


