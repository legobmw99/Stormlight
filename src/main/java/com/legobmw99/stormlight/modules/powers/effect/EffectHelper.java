package com.legobmw99.stormlight.modules.powers.effect;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class EffectHelper {

    private static final int BASE_TIME = 600; // 20 seconds

    public static boolean toggleEffect(Player player, MobEffect effect) {
        return toggleEffect(player, effect, 0, true, true);
    }

    public static boolean toggleEffect(Player player, MobEffect effect, int level, boolean ambient, boolean showicon) {
        if (player.hasEffect(effect)) {
            player.removeEffect(effect);
            return false;
        } else {
            player.addEffect(new MobEffectInstance(effect, MobEffectInstance.INFINITE_DURATION, level, ambient, false, showicon));
            return true;
        }
    }

    public static int increasePermanentEffect(Player player, MobEffect effect, int max) {
        int level = player.hasEffect(effect) ? player.getEffect(effect).getAmplifier() : -1;
        level = level < max ? level + 1 : max;
        player.addEffect(new MobEffectInstance(effect, MobEffectInstance.INFINITE_DURATION, level, true, false, true));
        return level;
    }

    public static int decreasePermanentEffect(Player player, MobEffect effect) {
        if (!player.hasEffect(effect)) {
            return -1;
        }
        int level = player.getEffect(effect).getAmplifier() - 1;
        player.removeEffect(effect);
        if (level >= 0) {
            player.addEffect(new MobEffectInstance(effect, MobEffectInstance.INFINITE_DURATION, level, true, false, true));
        }

        return level;
    }

    public static boolean drainStormlight(LivingEntity entity, int duration) {
        MobEffect stormlight = PowersSetup.STORMLIGHT.get();
        if (!entity.hasEffect(stormlight)) {
            return false;
        }
        MobEffectInstance effect = entity.getEffect(stormlight);
        if (effect.getDuration() < duration) {
            return false;
        }
        entity.removeEffect(stormlight);
        entity.addEffect(new MobEffectInstance(stormlight, effect.getDuration() - duration));
        return true;
    }

    public static void addOrUpdateEffect(Player player, int modifier) {
        addOrUpdateEffect(player, modifier, BASE_TIME);
    }

    public static void addOrUpdateEffect(Player player, int modifier, int baseTime) {
        int toAdd = baseTime * modifier;
        if (player.hasEffect(PowersSetup.STORMLIGHT.get())) {
            player.addEffect(new MobEffectInstance(PowersSetup.STORMLIGHT.get(), Math.min(toAdd + player.getEffect(PowersSetup.STORMLIGHT.get()).getDuration(), 36 * baseTime)));
        } else {
            player.addEffect(new MobEffectInstance(PowersSetup.STORMLIGHT.get(), toAdd));
        }
    }

}
