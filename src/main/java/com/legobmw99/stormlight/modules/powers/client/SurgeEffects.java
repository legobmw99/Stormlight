package com.legobmw99.stormlight.modules.powers.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class SurgeEffects {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void progressionEffect(@Nonnull BlockPos pos, boolean shiftHeld) {
        if (!shiftHeld) {
            BoneMealItem.addGrowthParticles(Minecraft.getInstance().level, pos, 0);
        }
    }

    public static void adhesionEffect(@Nonnull BlockPos blockPos, boolean shiftHeld) {
        if (!shiftHeld) {
            for (int i = 0; i < 15; i++) {
                Vec3 pos = mc.player.position().add(0, mc.player.getEyeHeight() / 1.5, 0);
                Vec3 motion = Vec3
                        .atBottomCenterOf(blockPos)
                        .add((new Vec3(mc.level.random.nextDouble(), mc.level.random.nextDouble(), mc.level.random.nextDouble())).normalize())
                        .subtract(pos)
                        .normalize();
                mc.level.addParticle(ParticleTypes.END_ROD, pos.x(), pos.y(), pos.z(), motion.x(), motion.y(), motion.z());

            }
        }
    }
}
