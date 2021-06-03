package com.legobmw99.stormlight.modules.powers.effect;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class EffectHelper {

    private static final int BASE_TIME = 600; // 20 seconds
    private static final int MAX_TIME = Integer.MAX_VALUE;

    public static boolean toggleEffect(PlayerEntity player, Effect effect) {
        return toggleEffect(player, effect, 0, true, true);
    }

    public static boolean toggleEffect(PlayerEntity player, Effect effect, int level, boolean ambient, boolean showicon) {
        if (player.hasEffect(effect)) {
            player.removeEffect(effect);
            return false;
        } else {
            player.addEffect(new EffectInstance(effect, MAX_TIME, level, ambient, false, showicon));
            return true;
        }
    }

    public static int increasePermanentEffect(PlayerEntity player, Effect effect, int max) {
        int level = player.hasEffect(effect) ? player.getEffect(effect).getAmplifier() : -1;
        level = level < max ? level + 1 : max;
        player.addEffect(new EffectInstance(effect, MAX_TIME, level, true, false, true));
        return level;
    }

    public static int decreasePermanentEffect(PlayerEntity player, Effect effect) {
        if (!player.hasEffect(effect)) {
            return -1;
        }
        int level = player.getEffect(effect).getAmplifier() - 1;
        player.removeEffect(effect);
        if (level >= 0) {
            player.addEffect(new EffectInstance(effect, MAX_TIME, level, true, false, true));
        }

        return level;
    }

    public static boolean drainStormlight(LivingEntity entity, int duration) {
        Effect stormlight = PowersSetup.STORMLIGHT.get();
        if (!entity.hasEffect(stormlight)) {
            return false;
        }
        EffectInstance effect = entity.getEffect(stormlight);
        if (effect.getDuration() < duration) {
            return false;
        }
        entity.removeEffect(stormlight);
        entity.addEffect(new EffectInstance(stormlight, effect.getDuration() - duration));
        return true;
    }

    public static void addOrUpdateEffect(PlayerEntity player, int modifier) {
        addOrUpdateEffect(player, modifier, BASE_TIME);
    }

    public static void addOrUpdateEffect(PlayerEntity player, int modifier, int baseTime) {
        int toAdd = baseTime * modifier;
        if (player.hasEffect(PowersSetup.STORMLIGHT.get())) {
            player.addEffect(new EffectInstance(PowersSetup.STORMLIGHT.get(), Math.min(toAdd + player.getEffect(PowersSetup.STORMLIGHT.get()).getDuration(), 36 * baseTime)));
        } else {
            player.addEffect(new EffectInstance(PowersSetup.STORMLIGHT.get(), toAdd));
        }
    }

}
