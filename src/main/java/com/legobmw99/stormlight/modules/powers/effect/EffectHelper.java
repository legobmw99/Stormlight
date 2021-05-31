package com.legobmw99.stormlight.modules.powers.effect;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

public class EffectHelper {

    private static final int BASE_TIME = 400; // 20 seconds
    private static final int MAX_TIME = Integer.MAX_VALUE;

    public static boolean toggleEffect(PlayerEntity player, Effect effect) {
        return toggleEffect(player, effect, 0, true, true);
    }

    public static boolean toggleEffect(PlayerEntity player, Effect effect, int level, boolean ambient, boolean visible) {
        if (player.hasEffect(effect)) {
            player.removeEffect(effect);
            return false;
        } else {
            player.addEffect(new EffectInstance(effect, MAX_TIME, level, ambient, visible));
            return true;
        }
    }

    public static void addOrUpdateEffect(PlayerEntity player, int modifier) {
        addOrUpdateEffect(player, modifier, BASE_TIME);
    }


    public static void addOrUpdateEffect(PlayerEntity player, int modifier, int baseTime) {
        int toAdd = baseTime * modifier;
        if (player.hasEffect(PowersSetup.STORMLIGHT.get())) {
            player.addEffect(new EffectInstance(PowersSetup.STORMLIGHT.get(), Math.min(toAdd + player.getEffect(PowersSetup.STORMLIGHT.get()).getDuration(), 24 * baseTime)));
        } else {
            player.addEffect(new EffectInstance(PowersSetup.STORMLIGHT.get(), toAdd));
        }
    }

    public static class GenericEffect extends Effect {
        public GenericEffect(EffectType type, int color) {
            super(type, color);
        }

    }

    public static class StormlightEffect extends GenericEffect {
        public StormlightEffect(int color) {
            super(EffectType.BENEFICIAL, color);
        }

        @Override
        public void applyEffectTick(LivingEntity entity, int amplifier) {
            if (!entity.hasEffect(PowersSetup.STORMLIGHT.get())) {
                entity.removeEffect(this);
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return true;
        }
    }
}
