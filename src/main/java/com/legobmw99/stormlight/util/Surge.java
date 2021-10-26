package com.legobmw99.stormlight.util;

import com.legobmw99.stormlight.modules.powers.Surges;
import com.legobmw99.stormlight.modules.powers.client.SurgeEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

public enum Surge {
    ADHESION(Surges::adhesion, 25, true, SurgeEffects::adhesionEffect),
    GRAVITATION(Surges::gravitation),
    DIVISION(Surges::division),
    ABRASION(Surges::abrasion),
    PROGRESSION(Surges::progression, SurgeEffects::progressionEffect),
    ILLUMINATION(Surges::illumination),
    TRANSFORMATION(Surges::transformation),
    TRANSPORTATION(Surges::transportation, 40F),
    COHESION(Surges::cohesion),
    TENSION(Surges::tension);

    private final ISurgePower surge;
    private final ISurgeEffect effect;
    private final float range;
    private final boolean repeating;

    Surge(ISurgePower surge) {
        this(surge, 30F, false, null);
    }

    Surge(ISurgePower surge, float range) {
        this(surge, range, false, null);
    }

    Surge(ISurgePower surge, float range, boolean repeating) {
        this(surge, range, repeating, null);
    }

    Surge(ISurgePower surge, ISurgeEffect effect) {
        this(surge, 30F, false, effect);
    }

    Surge(ISurgePower surge, float range, boolean repeating, ISurgeEffect effect) {
        this.surge = surge;
        this.range = range;
        this.repeating = repeating;
        this.effect = effect;
    }


    public boolean hasEffect() {
        return effect != null;
    }

    public boolean isRepeating() {return repeating;}

    public float getRange() {
        return range;
    }

    public void fire(ServerPlayer player, @Nullable BlockPos looking, boolean modified) {
        surge.fire(player, looking, modified);
    }

    public void displayEffect(BlockPos looking, boolean modified) {
        if (hasEffect()) {
            effect.fire(looking, modified);
        }
    }

}

@FunctionalInterface
interface ISurgePower {
    void fire(ServerPlayer player, @Nullable BlockPos looking, boolean modified);
}

@FunctionalInterface
interface ISurgeEffect {
    void fire(BlockPos looking, boolean modified);
}