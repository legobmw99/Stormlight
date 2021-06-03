package com.legobmw99.stormlight.modules.powers;

import com.legobmw99.stormlight.modules.combat.item.ShardbladeItem;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingDataProvider;
import com.legobmw99.stormlight.modules.powers.effect.EffectHelper;
import com.legobmw99.stormlight.modules.world.item.SphereItem;
import com.legobmw99.stormlight.network.Network;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PowersEventHandler {


    @SubscribeEvent
    public static void onAttachCapability(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            SurgebindingDataProvider provider = new SurgebindingDataProvider();
            event.addCapability(SurgebindingCapability.IDENTIFIER, provider);
            event.addListener(provider::invalidate);
        }
    }

    @SubscribeEvent
    public static void onJoinWorld(final PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            if (event.getPlayer() instanceof ServerPlayerEntity) {
                Network.sync(event.getPlayer());
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDeath(final LivingDeathEvent event) {
        if (!event.isCanceled() && event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            player.getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(data -> {
                if (data.isKnight() && !data.isBladeStored()) {
                    player.inventory.items
                            .stream()
                            .filter(is -> is.getItem() instanceof ShardbladeItem && ((ShardbladeItem) is.getItem()).getOrder() == data.getOrder())
                            .findFirst()
                            .ifPresent(blade -> {
                                data.storeBlade(blade);
                                blade.setCount(0);
                            });
                }
            });

        }

    }


    @SubscribeEvent
    public static void onPlayerClone(final PlayerEvent.Clone event) {
        if (!event.getPlayer().level.isClientSide()) {
            PlayerEntity player = event.getPlayer();
            player.getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(data -> {
                event.getOriginal().getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(oldData -> {
                    SurgebindingCapability.PLAYER_CAP.readNBT(data, null, SurgebindingCapability.PLAYER_CAP.writeNBT(oldData, null));
                });

            });
            Network.sync(player);

        }
    }

    @SubscribeEvent
    public static void onRespawn(final PlayerEvent.PlayerRespawnEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            Network.sync(event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(final PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            Network.sync(event.getPlayer());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamage(final LivingAttackEvent event) {
        if (event.getEntityLiving().hasEffect(PowersSetup.STORMLIGHT.get()) && (event.getSource() == DamageSource.IN_WALL || event.getSource() == DamageSource.DROWN)) {
            if (EffectHelper.drainStormlight(event.getEntityLiving(), 200)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().hasEffect(PowersSetup.STORMLIGHT.get())) {
            event.getEntityLiving().setGlowing(!event.getEntityLiving().hasEffect(Effects.INVISIBILITY));
            event.getEntityLiving().fallDistance = 0;
        } else {
            event.getEntityLiving().setGlowing(false);
            event.getEntityLiving().setNoGravity(false);
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
                    event.getPlayer().getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(data -> {
                        if (data.isKnight() && !data.isBladeStored()) {
                            //Only store correct type of blade
                            if (((ShardbladeItem) item).getOrder() == data.getOrder()) {
                                data.storeBlade(event.getEntityItem().getItem());
                                event.getEntity().kill();
                                event.setCanceled(true);
                                if (!entity.level.isClientSide()) {
                                    Network.sync(event.getPlayer());
                                }
                            }
                        }
                    });
                }
            }
            // Transform dropped spheres
            if (event.getEntity().level.isThundering() && item instanceof SphereItem) {
                double x, y, z;
                x = entity.getX();
                y = entity.getY();
                z = entity.getZ();
                if (event.getEntity().isAlive()) {
                    event.getEntity().kill();

                    for (int i = 0; i < event.getEntityItem().getItem().getCount(); i++) {
                        ItemEntity newEntity = new ItemEntity(event.getEntity().level, x, y, z,
                                                              new ItemStack(((SphereItem) item).swap(), 1, event.getEntityItem().getItem().getTag()));
                        event.getEntity().level.addFreshEntity(newEntity);
                    }

                    event.setCanceled(true);
                }
            }
        }

    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
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
}
