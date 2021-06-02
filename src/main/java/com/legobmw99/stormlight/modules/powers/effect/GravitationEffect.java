package com.legobmw99.stormlight.modules.powers.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

public class GravitationEffect extends StormlightEffect {

    public GravitationEffect() {
        super(6605055);
        this.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "a81758d2-c355-11eb-8529-0242ac130003", -0.02, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        entity.setNoGravity(amplifier == 4 && entity.hasEffect(this));
    }
}
