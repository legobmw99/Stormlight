package com.legobmw99.stormlight.modules.powers.effect;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;

public class StormlightEffect extends GenericEffect {
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
