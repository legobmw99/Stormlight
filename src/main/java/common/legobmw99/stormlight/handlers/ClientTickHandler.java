package common.legobmw99.stormlight.handlers;

import java.util.List;

import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.network.packets.BoundBladePacket;
import common.legobmw99.stormlight.network.packets.EffectEntityPacket;
import common.legobmw99.stormlight.util.Registry;
import elucent.albedo.event.GatherLightsEvent;
import elucent.albedo.lighting.Light;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientTickHandler {

	@SideOnly(Side.CLIENT)
	@Optional.Method(modid="albedo")
	@SubscribeEvent
	public void onLightGather(GatherLightsEvent event){
		List<EntityPlayer> pList = Minecraft.getMinecraft().player.world.playerEntities;
		for(EntityPlayer p: pList){
			if(p.isPotionActive(Registry.effectStormlight)){
				event.getLightList().add(Light.builder().pos(p).radius(4.0F).color(0.4F, 0.6F, 1.0F).build());
			}
		}
		List<Entity> eList = Minecraft.getMinecraft().player.world.getLoadedEntityList();
		for(Entity e: eList){
			if(e instanceof EntityItem){
				if(((EntityItem)e).getItem().isItemEqual(new ItemStack(Item.getByNameOrId("stormlight:sphere.charged")))){
					event.getLightList().add(Light.builder().pos(e).radius(2F).color(0.4F, 0.6F, 1.0F).build());
				}
			} else if (e instanceof EntityItemFrame){
				if(((EntityItemFrame)e).getDisplayedItem().isItemEqual(new ItemStack(Item.getByNameOrId("stormlight:sphere.charged")))){
					event.getLightList().add(Light.builder().pos(e).radius(2F).color(0.4F, 0.6F, 1.0F).build());
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		EntityPlayerSP player;
		player = Minecraft.getMinecraft().player;
		if(player != null){
			//Recall
			if(Registry.Recall.isPressed()){
				if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getUnlocalizedName().contains("honorblade.")){
					String type = player.getHeldItemMainhand().getUnlocalizedName().substring(16);
					Registry.network.sendToServer(new BoundBladePacket(Minecraft.getMinecraft().player.getEntityId(),type, 0));
				} else {
					Registry.network.sendToServer(new BoundBladePacket(Minecraft.getMinecraft().player.getEntityId(),"", 1));
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
				Registry.network.sendToServer(new EffectEntityPacket(25,1, 0, Minecraft.getMinecraft().player.getEntityId()));
			}
		}
	}
}
