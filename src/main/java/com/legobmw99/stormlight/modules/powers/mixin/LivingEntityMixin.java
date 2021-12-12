package com.legobmw99.stormlight.modules.powers.mixin;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow(remap = false)
    private Optional<BlockPos> lastClimbablePos;

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    // todo actually mixin to IForgeBlockState?
    @Inject(at = @At("RETURN"), method = "onClimbable", cancellable = true, remap = false)
    public void doCohesionClimb(CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue()) {
            LivingEntity entity = (LivingEntity) (Entity) this;
            if (entity.hasEffect(PowersSetup.STICKING.get()) && entity.level.getBlockCollisions(entity, entity.getBoundingBox().inflate(0.15, 0, 0.15)).iterator().hasNext()) {
                this.lastClimbablePos = Optional.of(blockPosition());
                info.setReturnValue(true);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "canStandOnFluid(Lnet/minecraft/world/level/material/Fluid;)Z", cancellable = true, remap = false)
    public void doTensionStand(Fluid fluid, CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue()) {
            LivingEntity entity = (LivingEntity) (Entity) this;
            if (entity.hasEffect(PowersSetup.TENSION.get())) {
                info.setReturnValue(true);
            }
        }
    }
}

