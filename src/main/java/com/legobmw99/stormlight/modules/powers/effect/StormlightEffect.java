package com.legobmw99.stormlight.modules.powers.effect;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class StormlightEffect extends GenericEffect {
    public StormlightEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
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
