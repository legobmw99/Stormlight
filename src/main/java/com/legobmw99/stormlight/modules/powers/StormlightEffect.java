package com.legobmw99.stormlight.modules.powers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

public class StormlightEffect extends Effect {
    protected StormlightEffect() {
        super(EffectType.BENEFICIAL, 0);
    }

    public static void addOrUpdateEffect(PlayerEntity player, int modifier){
        int toAdd = 4800 * modifier;
        if (player.hasEffect(PowersSetup.STORMLIGHT.get())) {
            player.addEffect(
                    new EffectInstance(PowersSetup.STORMLIGHT.get(), Math.min(toAdd + player.getEffect(PowersSetup.STORMLIGHT.get()).getDuration(), 19200)));
        } else {
            player.addEffect(new EffectInstance(PowersSetup.STORMLIGHT.get(), toAdd));
        }
    }
}
