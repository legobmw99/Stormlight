package com.legobmw99.stormlight.modules.powers.client;

import com.legobmw99.stormlight.modules.combat.item.ShardbladeItem;
import com.legobmw99.stormlight.modules.powers.PowersSetup;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.network.Network;
import com.legobmw99.stormlight.network.packets.SummonBladePacket;
import com.legobmw99.stormlight.network.packets.SurgePacket;
import com.legobmw99.stormlight.util.Surge;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.*;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientPowerUtils {

    private static final Minecraft mc = Minecraft.getInstance();

    /**
     * Adapted from vanilla, allows getting mouseover at given distances
     *
     * @param dist the distance requested
     * @return a RayTraceResult for the requested raytrace
     */
    @Nullable
    protected static HitResult getMouseOverExtended(float dist) {
        float partialTicks = mc.getFrameTime();
        HitResult objectMouseOver = null;
        Entity entity = mc.getCameraEntity();
        if (entity != null) {
            if (mc.level != null) {
                objectMouseOver = entity.pick(dist, partialTicks, false);
                Vec3 vec3d = entity.getEyePosition(partialTicks);
                double d1 = objectMouseOver.getLocation().distanceToSqr(vec3d);

                Vec3 vec3d1 = entity.getViewVector(1.0F);
                Vec3 vec3d2 = vec3d.add(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist);
                AABB axisalignedbb = entity.getBoundingBox().expandTowards(vec3d1.scale(dist)).inflate(1.0D, 1.0D, 1.0D);
                EntityHitResult entityraytraceresult = ProjectileUtil.getEntityHitResult(entity, vec3d, vec3d2, axisalignedbb, (e) -> true, d1);
                if (entityraytraceresult != null) {
                    Vec3 vec3d3 = entityraytraceresult.getLocation();
                    double d2 = vec3d.distanceToSqr(vec3d3);
                    if (d2 < d1) {
                        objectMouseOver = entityraytraceresult;
                    }
                }

            }
        }
        return objectMouseOver;
    }

    protected static Set<BlockPos> getEyePos(LivingEntity entity, float rangeX, float rangeY, float rangeZ) {
        Vec3 pos = entity.position().add(0, entity.getEyeHeight(entity.getPose()), 0);
        AABB cameraBox = new AABB(pos, pos);
        cameraBox = cameraBox.inflate(rangeX, rangeY, rangeZ);
        return BlockPos.betweenClosedStream(cameraBox).map(BlockPos::immutable).collect(Collectors.toSet());
    }

    @Nullable
    protected static BlockPos getMouseBlockPos(float dist) {
        HitResult trace = getMouseOverExtended(dist);
        if (trace != null) {
            if (trace.getType() == HitResult.Type.BLOCK) {
                return ((BlockHitResult) trace).getBlockPos();
            }
            if (trace.getType() == HitResult.Type.ENTITY) {
                return ((EntityHitResult) trace).getEntity().blockPosition();
            }
        }
        return null;
    }

    protected static void fireSurge(Surge surge) {
        boolean shiftDown = Minecraft.getInstance().options.keyShift.isDown();
        BlockPos pos = getMouseBlockPos(surge.getRange());
        //        if (pos != null) {
        //            surge.displayEffect(pos, shiftDown);
        //        }
        Network.sendToServer(new SurgePacket(surge, pos, shiftDown));
    }

    /**
     * Handles either mouse or button presses for the mod's keybinds
     */
    protected static void acceptInput(final int action) {
        Player player = mc.player;
        if (mc.screen == null) {
            if (player == null || !mc.isWindowActive()) {
                return;
            }


            player.getCapability(SurgebindingCapability.PLAYER_CAP).ifPresent(data -> {

                if (data.isKnight()) {

                    if (PowersClientSetup.blade.isDown() && (data.isBladeStored() || player.getMainHandItem().getItem() instanceof ShardbladeItem)) {
                        Network.sendToServer(new SummonBladePacket());
                        PowersClientSetup.blade.setDown(false);
                    }

                    if (player.hasEffect(PowersSetup.STORMLIGHT.get())) {
                        if (PowersClientSetup.firstSurge.isDown() && (action == GLFW.GLFW_PRESS || data.getOrder().getFirst().isRepeating())) {
                            fireSurge(data.getOrder().getFirst());
                        }

                        if (PowersClientSetup.secondSurge.isDown() && (action == GLFW.GLFW_PRESS || data.getOrder().getSecond().isRepeating())) {
                            fireSurge(data.getOrder().getSecond());
                        }
                    }
                }
            });
        }
    }
}
