package com.legobmw99.stormlight.modules.powers.client;

import net.minecraft.client.Minecraft;
import net.minecraft.item.BoneMealItem;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class SurgeEffects {

    public static void progressionEffect(@Nonnull BlockPos pos, boolean shiftHeld) {
        if (!shiftHeld) {
            BoneMealItem.addGrowthParticles(Minecraft.getInstance().level, pos, 0);
        }
    }

    public static void adhesionEffect(@Nonnull BlockPos pos, boolean shiftHeld) {
        // todo
        if (!shiftHeld) {
            BoneMealItem.addGrowthParticles(Minecraft.getInstance().level, pos, 0);
        }
    }
}
