package com.legobmw99.stormlight.modules.powers;

import com.legobmw99.stormlight.modules.combat.item.ShardbladeItem;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingDataProvider;
import com.legobmw99.stormlight.modules.powers.effect.EffectHelper;
import com.legobmw99.stormlight.modules.world.item.SphereItem;
import com.legobmw99.stormlight.network.Network;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.ServerLevelData;
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
        if (event.getObject() instanceof Player) {
            SurgebindingDataProvider provider = new SurgebindingDataProvider();
            event.addCapability(SurgebindingCapability.IDENTIFIER, provider);
        }
    }

    @SubscribeEvent
    public static void onJoinWorld(final PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            if (event.getPlayer() instanceof ServerPlayer) {
                Network.sync(event.getPlayer());
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDeath(final LivingDeathEvent event) {
        if (!event.isCanceled() && event.getEntityLiving() instanceof Player) {
            Player player = (Player) event.getEntityLiving();
            player.getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(data -> {
                if (data.isKnight() && !data.isBladeStored()) {
                    player.getInventory().items
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
            event.getOriginal().reviveCaps();
            Player player = event.getPlayer();
            player.getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(data -> {
                event.getOriginal().getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(oldData -> {
                    data.load(oldData.save());
                });

            });
            event.getOriginal().getCapability(SurgebindingCapability.PLAYER_CAP).invalidate();
            event.getOriginal().invalidateCaps();

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
            event.getEntityLiving().setGlowingTag(!event.getEntityLiving().hasEffect(MobEffects.INVISIBILITY));
            event.getEntityLiving().fallDistance = 0;
        } else {
            event.getEntityLiving().setGlowingTag(false);
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
            LevelData data = event.world.getLevelData();
            if (data instanceof ServerLevelData) {
                ServerLevelData info = (ServerLevelData) data;
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
