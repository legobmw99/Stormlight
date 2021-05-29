package com.legobmw99.stormlight.modules.powers.client;

import com.legobmw99.stormlight.modules.combat.item.ShardbladeItem;
import com.legobmw99.stormlight.modules.powers.PowersSetup;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.network.Network;
import com.legobmw99.stormlight.network.packets.SummonBladePacket;
import com.legobmw99.stormlight.network.packets.SurgePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;

public class PowerClientEventHandler {

    private static final Minecraft mc = Minecraft.getInstance();

    /**
     * Adapted from vanilla, allows getting mouseover at given distances
     *
     * @param dist the distance requested
     * @return a RayTraceResult for the requested raytrace
     */
    @Nullable
    public static BlockPos getMouseOverExtended(float dist) {
        float partialTicks = mc.getFrameTime();
        RayTraceResult objectMouseOver = null;
        Entity entity = mc.getCameraEntity();
        if (entity != null) {
            if (mc.level != null) {
                objectMouseOver = entity.pick(dist, partialTicks, false);
                Vector3d vec3d = entity.getEyePosition(partialTicks);
                boolean flag = false;
                int i = 3;
                double d1;

                d1 = objectMouseOver.getLocation().distanceToSqr(vec3d);

                Vector3d vec3d1 = entity.getViewVector(1.0F);
                Vector3d vec3d2 = vec3d.add(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist);
                float f = 1.0F;
                AxisAlignedBB axisalignedbb = entity.getBoundingBox().expandTowards(vec3d1.scale(dist)).inflate(1.0D, 1.0D, 1.0D);
                EntityRayTraceResult entityraytraceresult = ProjectileHelper.getEntityHitResult(entity, vec3d, vec3d2, axisalignedbb, (e) -> true, d1);
                if (entityraytraceresult != null) {
                    Entity entity1 = entityraytraceresult.getEntity();
                    Vector3d vec3d3 = entityraytraceresult.getLocation();
                    double d2 = vec3d.distanceToSqr(vec3d3);
                    if (d2 < d1) {
                        objectMouseOver = entityraytraceresult;
                    }
                }

            }
        }
        return new BlockPos(objectMouseOver.getLocation());

    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        acceptInput(event.getAction());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onMouseInput(final InputEvent.MouseInputEvent event) {
        acceptInput(event.getAction());

    }

    /**
     * Handles either mouse or button presses for the mod's keybinds
     */
    private void acceptInput(int action) {
        if (action == GLFW.GLFW_PRESS) {// || action == GLFW.GLFW_REPEAT){
            PlayerEntity player = mc.player;
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
                            if (PowersClientSetup.firstSurge.isDown()) {

                                Network.sendToServer(new SurgePacket(data.getOrder().getFirst(), getMouseOverExtended(30f), Minecraft.getInstance().options.keyShift.isDown()));
                            }

                            if (PowersClientSetup.secondSurge.isDown()) {
                                Network.sendToServer(new SurgePacket(data.getOrder().getSecond(), getMouseOverExtended(30f), Minecraft.getInstance().options.keyShift.isDown()));
                            }
                        }
                    }
                });
            }
        }
    }
}
