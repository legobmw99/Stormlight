package common.legobmw99.stormlight.handlers;

import java.awt.Event;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.items.Honorblade;
import common.legobmw99.stormlight.network.packets.BoundBladePacket;
import common.legobmw99.stormlight.network.packets.EffectEntityPacket;
import common.legobmw99.stormlight.network.packets.MoveEntityPacket;
import common.legobmw99.stormlight.network.packets.StopFallPacket;
import common.legobmw99.stormlight.util.Registry;

public class StormlightTickHandler {


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

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event){
		if(event.getEntityLiving().isPotionActive(Registry.effectStormlight)){
			event.getEntityLiving().setGlowing(true);
			Registry.network.sendToServer(new StopFallPacket());
		}else{
			if(event.getEntityLiving().isPotionActive(Potion.getPotionById(25)) && event.getEntityLiving().dimension == 0){
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

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		EntityPlayerSP player;
		player = Minecraft.getMinecraft().thePlayer;
		if(player != null){
			//Recall
			if(Registry.Recall.isPressed()){
				if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getUnlocalizedName().contains("honorblade.")){
					String type = player.getHeldItemMainhand().getUnlocalizedName().substring(16);
					Registry.network.sendToServer(new BoundBladePacket(Minecraft.getMinecraft().thePlayer.getEntityId(),type, 0));
				} else {
					Registry.network.sendToServer(new BoundBladePacket(Minecraft.getMinecraft().thePlayer.getEntityId(),"", 1));
				}
			}
			//Surges
			if(player.isPotionActive(Registry.effectStormlight)){
				//Windrunners
				if(player.inventory.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.windrunners")))){
					if(Registry.BindingOne.isPressed()){
						if(Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()){
							Stormlight.surges.gravitation(player, 1);
						}else {
							Stormlight.surges.gravitation(player, 0);
						}
					}
					if(Registry.BindingTwo.isPressed()){

					}
					
				}
				//Skybreakers
				if(player.inventory.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.skybreakers")))){
					if(Registry.BindingOne.isPressed()){
						
					}
					if(Registry.BindingTwo.isPressed()){
						if(Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()){
							Stormlight.surges.gravitation(player, 1);
						}else {
							Stormlight.surges.gravitation(player, 0);
						}
					}
					
				}
				//Elsecallers
				if(player.inventory.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.elsecallers")))){
					if(Registry.BindingOne.isPressed()){
						if(Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()){
							Stormlight.surges.transportation(player, 1);
						}else {
							Stormlight.surges.transportation(player, 0);
						}
					}
					if(Registry.BindingTwo.isPressed()){
						Stormlight.surges.transformation(player);
					}
				}
				//Edgedancers
				if(player.inventory.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.edgedancers")))){
					if(Registry.BindingOne.isPressed()){
						if(Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()){
							Stormlight.surges.progression(player, 1);
						}else {
							Stormlight.surges.progression(player, 0);
						}		
					}
					if(Registry.BindingTwo.isPressed()){
									
					}
				}
				//Truthwatchers
				if(player.inventory.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.truthwatchers")))){
					if(Registry.BindingOne.isPressed()){
						
					}
					if(Registry.BindingTwo.isPressed()){
						if(Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()){
							Stormlight.surges.progression(player, 1);
						}else {
							Stormlight.surges.progression(player, 0);
						}					
					}
				}
				//Lightweavers
				if(player.inventory.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.lightweavers")))){
					if(Registry.BindingOne.isPressed()){
						Stormlight.surges.transformation(player);
					}
					if(Registry.BindingTwo.isPressed()){
				
					}
				}
				//Willshapers
				if(player.inventory.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.willshapers")))){
					if(Registry.BindingOne.isPressed()){
					}
					if(Registry.BindingTwo.isPressed()){
						if(Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()){
							Stormlight.surges.transportation(player, 1);
						}else {
							Stormlight.surges.transportation(player, 0);
						}
					}
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
}


