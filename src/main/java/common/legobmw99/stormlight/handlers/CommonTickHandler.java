package common.legobmw99.stormlight.handlers;

import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.items.ItemShardblade;
import common.legobmw99.stormlight.network.packets.StormlightCapabilityPacket;
import common.legobmw99.stormlight.util.Registry;
import common.legobmw99.stormlight.util.StormlightCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class CommonTickHandler {

	@SubscribeEvent
	public void onEntityAttachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer
				&& !event.getObject().hasCapability(Stormlight.PLAYER_CAP, null)) {
			event.addCapability(StormlightCapability.IDENTIFIER, new StormlightCapability());
		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		// the dead player's cap
		StormlightCapability oldCap = StormlightCapability.forPlayer(event.getOriginal());
		// the clone's cap
		StormlightCapability cap = StormlightCapability.forPlayer(event.getEntityPlayer());
		if (oldCap.getType() >= 0) {
			cap.setType(oldCap.getType()); // make sure the new player has the same status
			cap.setProgression(oldCap.getProgression());
		}
	}

	@SubscribeEvent
	public void onPlayerLogin(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
			StormlightCapability cap = StormlightCapability.forPlayer(player);
			Registry.network.sendTo(new StormlightCapabilityPacket(cap), player);
		}
	}

	@SubscribeEvent
	public void onDamage(LivingHurtEvent event) {
		// Increase outgoing damage for stormlit people
		if (event.getSource().getTrueSource() instanceof EntityPlayerMP) {
			EntityPlayerMP source = (EntityPlayerMP) event.getSource().getTrueSource();
			if (source.isPotionActive(Registry.effectStormlight)) {
				event.setAmount(event.getAmount() + 2);
			}
		}
		
		//Don't hurt Dustbringers with their own explosions
		if(event.getSource().getTrueSource() == event.getEntityLiving() && event.getSource().getDamageType().equals("explosion")){
			event.setCanceled(true);
		}
		//Don't hurt their pets either
		if(event.getEntityLiving() instanceof EntityTameable && ((EntityTameable)event.getEntityLiving()).getOwner() == event.getSource().getTrueSource() && event.getSource().getDamageType().equals("explosion")){
			event.setCanceled(true);
		}
		
		// Reduce incoming damage for stormlit people
		if (event.getEntityLiving() instanceof EntityPlayerMP) {
			EntityPlayerMP source = (EntityPlayerMP) event.getEntityLiving();
			if (source.isPotionActive(Registry.effectStormlight)) {
				event.setAmount(event.getAmount() - 2);
			}
		}
	}

	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		Registry.initItems(event);
	}

	@SubscribeEvent
	public void onRegister(RegistryEvent.Register<Potion> event) {
		Registry.registerEffect(event);
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
		if (event.getEntityLiving().isPotionActive(Registry.effectStormlight)) {
			event.getEntityLiving().setGlowing(true);
			event.getEntityLiving().fallDistance = 0;
		} else {
			event.getEntityLiving().setGlowing(false);
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onItemToss(ItemTossEvent event) {
		if (event.getEntityItem() != null) {
			// Store dropped shardblades
			if (event.getEntityItem().getItem().getItem() instanceof ItemShardblade) {
				if (event.getPlayer() != null) {
					StormlightCapability cap = StormlightCapability.forPlayer(event.getPlayer());
					if (cap != null && cap.getType() >= 0 && !cap.isBladeStored()) {
						//Only store correct type of blade
						if (event.getEntityItem().getItem().getItemDamage() == cap.getType()) {
							event.getEntity().setDead();
							cap.setBladeStored(true);
							event.setCanceled(true);
						}
					}
				}
			}
			// Transform dropped spheres
			if (event.getEntity().getEntityWorld().isThundering()
					&& event.getEntityItem().getItem().isItemEqual(new ItemStack(Registry.itemSphere, 1, 0))) {
				double x, y, z;
				int a;
				x = event.getEntityItem().posX;
				y = event.getEntityItem().posY;
				z = event.getEntityItem().posZ;
				a = event.getEntityItem().getItem().getCount();
				EntityItem entity = new EntityItem(event.getEntity().getEntityWorld(), x, y, z,
						new ItemStack(Registry.itemSphere, a, 1));
				if (event.getEntity().isEntityAlive()) {
					event.getEntity().getEntityWorld().spawnEntity(entity);
					event.getEntity().setDead();
					
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		WorldInfo info = event.world.getWorldInfo();
		if (info.getWorldTime() % 96000 == 0) {
			info.setCleanWeatherTime(0);
			info.setRainTime(2400);
			info.setThunderTime(2400);
			info.setRaining(true);
			info.setThundering(true);
		}
	}
}