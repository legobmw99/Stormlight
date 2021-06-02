package com.legobmw99.stormlight.modules.powers.mixin;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    private Optional<BlockPos> lastClimbablePos;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // todo actually mixin to IForgeBlockState
    @Inject(at = @At("RETURN"), method = "onClimbable", cancellable = true)
    public void doCohesionClimb(CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue()) {
            if ((Entity) this instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) (Entity) this;
                if (player.hasEffect(PowersSetup.STICKING.get()) &&
                    (player.level.getBlockCollisions(player, player.getBoundingBox().move(0.15, 0, 0.15)).findFirst().isPresent() ||
                     player.level.getBlockCollisions(player, player.getBoundingBox().move(-0.15, 0, -0.15)).findFirst().isPresent())) {
                    this.lastClimbablePos = Optional.of(blockPosition());
                    info.setReturnValue(true);
                }

            }
        }
    }
}

