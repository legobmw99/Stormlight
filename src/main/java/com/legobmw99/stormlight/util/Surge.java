package com.legobmw99.stormlight.util;

import com.legobmw99.stormlight.modules.powers.Surges;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public enum Surge {
    ADHESION(Surges::test),
    GRAVITATION(Surges::gravitation),
    DIVISION(Surges::division),
    ABRASION(Surges::abrasion),
    PROGRESSION(Surges::progression),
    ILLUMINATION(Surges::illumination),
    TRANSFORMATION(Surges::test),
    TRANSPORTATION(Surges::test),
    COHESION(Surges::cohesion),
    TENSION(Surges::test);

    private final ISurgePower surge;

    Surge(ISurgePower surge) {
        this.surge = surge;
    }

    public void fire(ServerPlayerEntity player, @Nullable BlockPos looking, boolean modified) {
        surge.fire(player, looking, modified);
    }

}

@FunctionalInterface
interface ISurgePower {
    void fire(ServerPlayerEntity player, @Nullable BlockPos looking, boolean modified);
}

