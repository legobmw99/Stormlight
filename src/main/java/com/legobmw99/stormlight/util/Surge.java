package com.legobmw99.stormlight.util;

import com.legobmw99.stormlight.modules.powers.Surges;
import com.legobmw99.stormlight.modules.powers.client.SurgeEffects;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public enum Surge {
    ADHESION(Surges::adhesion, SurgeEffects::adhesionEffect),
    GRAVITATION(Surges::gravitation),
    DIVISION(Surges::division),
    ABRASION(Surges::abrasion),
    PROGRESSION(Surges::progression, SurgeEffects::progressionEffect),
    ILLUMINATION(Surges::illumination),
    TRANSFORMATION(Surges::transformation),
    TRANSPORTATION(Surges::test),
    COHESION(Surges::cohesion),
    TENSION(Surges::test);

    private final ISurgePower surge;
    private final ISurgeEffect effect;

    Surge(ISurgePower surge) {
        this.surge = surge;
        this.effect = null;
    }

    Surge(ISurgePower surge, ISurgeEffect effect) {
        this.surge = surge;
        this.effect = effect;
    }

    public boolean hasEffect() {
        return effect != null;
    }


    public void fire(ServerPlayerEntity player, @Nullable BlockPos looking, boolean modified) {
        surge.fire(player, looking, modified);
    }

    @OnlyIn(Dist.CLIENT)
    public void displayEffect(BlockPos looking, boolean modified) {
        if (hasEffect()) {
            effect.fire(looking, modified);
        }
    }

}

@FunctionalInterface
interface ISurgePower {
    void fire(ServerPlayerEntity player, @Nullable BlockPos looking, boolean modified);
}

@FunctionalInterface
interface ISurgeEffect {
    void fire(BlockPos looking, boolean modified);
}