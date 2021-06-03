package com.legobmw99.stormlight.modules.powers.client;

import net.minecraft.client.Minecraft;
import net.minecraft.item.BoneMealItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

public class SurgeEffects {
    private static Minecraft mc = Minecraft.getInstance();

    public static void progressionEffect(@Nonnull BlockPos pos, boolean shiftHeld) {
        if (!shiftHeld) {
            BoneMealItem.addGrowthParticles(Minecraft.getInstance().level, pos, 0);
        }
    }

    public static void adhesionEffect(@Nonnull BlockPos blockPos, boolean shiftHeld) {
        if (!shiftHeld) {
            for (int i = 0; i < 15; i++) {
                Vector3d pos = mc.player.position().add(0, mc.player.getEyeHeight() / 1.5, 0);
                Vector3d motion = Vector3d
                        .atBottomCenterOf(blockPos)
                        .add((new Vector3d(mc.level.random.nextDouble(), mc.level.random.nextDouble(), mc.level.random.nextDouble())).normalize())
                        .subtract(pos)
                        .normalize();
                mc.level.addParticle(ParticleTypes.END_ROD, pos.x(), pos.y(), pos.z(), motion.x(), motion.y(), motion.z());

            }
        }
    }
}
