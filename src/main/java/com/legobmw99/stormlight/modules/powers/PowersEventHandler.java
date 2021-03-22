package com.legobmw99.stormlight.modules.powers;

import com.legobmw99.stormlight.modules.combat.item.ShardbladeItem;
import com.legobmw99.stormlight.modules.world.item.SphereItem;
import com.legobmw99.stormlight.network.Network;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PowersEventHandler {


    @SubscribeEvent
    public static void onAttachCapability(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(StormlightCapability.IDENTIFIER, new StormlightCapability());
        }
    }

    @SubscribeEvent
    public static void onJoinWorld(final PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().level.isClientSide) {
            Network.sync(event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(final PlayerEvent.Clone event) {
        if (!event.getPlayer().level.isClientSide()) {

            PlayerEntity player = event.getPlayer();
            StormlightCapability cap = StormlightCapability.forPlayer(player); // the clone's cap

            PlayerEntity old = event.getOriginal();

            old.getCapability(StormlightCapability.PLAYER_CAP).ifPresent(oldCap -> {
                cap.deserializeNBT(oldCap.serializeNBT());
            });

            Network.sync(player);
        }
    }

    @SubscribeEvent
    public static void onRespawn(final PlayerEvent.PlayerRespawnEvent event) {
        if (!event.getPlayer().getCommandSenderWorld().isClientSide()) {
            Network.sync(event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(final PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getPlayer().getCommandSenderWorld().isClientSide()) {
            Network.sync(event.getPlayer());
        }
    }


    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().hasEffect(PowersSetup.STORMLIGHT.get())) {
            event.getEntityLiving().setGlowing(!event.getEntityLiving().hasEffect(Effects.INVISIBILITY));
            event.getEntityLiving().fallDistance = 0;
        } else {
            event.getEntityLiving().setGlowing(false);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onItemToss(ItemTossEvent event) {
        ItemEntity entity = event.getEntityItem();
        if (entity != null) {
            // Store dropped shardblades
            Item item = event.getEntityItem().getItem().getItem();
            if (item instanceof ShardbladeItem) {
                if (event.getPlayer() != null) {
                    StormlightCapability cap = StormlightCapability.forPlayer(event.getPlayer());
                    if (cap.isKnight() && !cap.isBladeStored()) {
                        //Only store correct type of blade
                        if (((ShardbladeItem) item).getOrder() == cap.getOrder()) {
                            cap.storeBlade(event.getEntityItem().getItem());
                            event.getEntity().kill();
                            event.setCanceled(true);
                        }
                    }
                }
            }
            // Transform dropped spheres
            if (event.getEntity().level.isThundering() && item instanceof SphereItem) {
                double x, y, z;
                int a;
                x = entity.getX();
                y = entity.getY();
                z = entity.getZ();
                a = event.getEntityItem().getItem().getCount();
                ItemEntity newEntity = new ItemEntity(event.getEntity().level, x, y, z, new ItemStack(((SphereItem) item).swap(), a, event.getEntityItem().getItem().getTag()));
                if (event.getEntity().isAlive()) {
                    event.getEntity().level.addFreshEntity(newEntity);
                    event.getEntity().kill();

                    event.setCanceled(true);
                }
            }
        }

    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        IWorldInfo data = event.world.getLevelData();
        if (data instanceof IServerWorldInfo) {
            IServerWorldInfo info = (IServerWorldInfo) data;
            if (info.getGameTime() % 96000 == 0) {
                info.setClearWeatherTime(0);
                info.setRainTime(2400);
                info.setThunderTime(2400);
                info.setRaining(true);
                info.setThundering(true);
            }
        }
    }
}
