package common.legobmw99.stormlight.handlers;

import java.util.List;

import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.network.packets.BoundBladePacket;
import common.legobmw99.stormlight.network.packets.EffectEntityPacket;
import common.legobmw99.stormlight.network.packets.StopFallPacket;
import common.legobmw99.stormlight.util.Registry;
import common.legobmw99.stormlight.util.StormlightCapability;
import elucent.albedo.event.GatherLightsEvent;
import elucent.albedo.lighting.Light;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonTickHandler {

	@SubscribeEvent
	public void onEntityAttachCapability(AttachCapabilitiesEvent<Entity> event){
		if (event.getObject() instanceof EntityPlayer && !event.getObject().hasCapability(Stormlight.PLAYER_CAP, null)) {
            event.addCapability(StormlightCapability.IDENTIFIER, new StormlightCapability());
        }
	}
	
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
    	StormlightCapability oldCap = StormlightCapability.forPlayer(event.getOriginal()); // the dead player's cap
    	StormlightCapability cap = StormlightCapability.forPlayer(event.getEntityPlayer()); // the clone's cap
        if (oldCap.getType() >= 0) {
            cap.setType(oldCap.getType()); // make sure the new player has the same  status
            cap.setProgression(oldCap.getProgression());
        }
      }

	@SubscribeEvent
	public void onDamage(LivingHurtEvent event) {
		//Increase outgoing damage for stormlit people
		if (event.getSource().getTrueSource() instanceof EntityPlayerMP) {
			EntityPlayerMP source = (EntityPlayerMP) event.getSource().getTrueSource();
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
    public void onRegisterItems(RegistryEvent.Register<Item> event){
    	Registry.initItems(event);
    }
    @SubscribeEvent
    public void onRegister(RegistryEvent.Register<Potion> event){
		Registry.registerEffect(event);
    }
    
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event){
		if(event.getEntityLiving().isPotionActive(Registry.effectStormlight)){
			event.getEntityLiving().setGlowing(true);
			Registry.network.sendToServer(new StopFallPacket());
		}else{
			if(event.getEntityLiving().isPotionActive(Potion.getPotionById(25)) && event.getEntityLiving().dimension == 0){
				Registry.network.sendToServer(new EffectEntityPacket(25,0, 0, Minecraft.getMinecraft().player.getEntityId()));
			}
			event.getEntityLiving().setGlowing(false);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onItemToss(ItemTossEvent event){
		if(event.getEntity().getEntityWorld().isThundering()){
			if(event.getEntityItem()!= null){
				if(event.getEntityItem().getItem().isItemEqual(new ItemStack(Registry.itemSphere))){
					double x,y,z;
					int a;
					x = event.getEntityItem().posX;
					y = event.getEntityItem().posY;
					z = event.getEntityItem().posZ;
					a = event.getEntityItem().getItem().getCount();
					EntityItem entity = new EntityItem(event.getEntity().getEntityWorld(), x, y, z, new ItemStack(Item.getByNameOrId("Stormlight:sphere.charged"), a, 0));
					if(event.getEntity().isEntityAlive()){
						event.getEntity().getEntityWorld().spawnEntity(entity);
						event.getEntity().setDead();
					}
				}
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