package com.legobmw99.stormlight.modules.powers.mixin;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(IForgeBlockState.class)
public interface IForgeBlockStateMixin {

    @Shadow(remap = false)
    private BlockState self() {
        return null;
    }

    /**
     * Add stormlight mod checks for slipperiness
     *
     * @author legobmw99
     * @reason unable to inject into default interface method
     */
    @Overwrite(remap = false)
    default float getFriction(LevelReader world, BlockPos pos, @Nullable Entity entity) {
        if (entity instanceof LivingEntity p && p.hasEffect(PowersSetup.SLICKING.get())) {
            return 0.989f;
        }
        if ((entity instanceof Boat && entity.hasPassenger(LivingEntity.class::isInstance) &&
             entity.getPassengers().stream().anyMatch(e -> e instanceof LivingEntity p && p.hasEffect(PowersSetup.SLICKING.get())))) {
            return 0.989f;
        }

        return self().getBlock().getFriction(self(), world, pos, entity);

    }
}
