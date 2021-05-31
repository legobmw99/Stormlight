package com.legobmw99.stormlight.modules.powers.mixin;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.extensions.IForgeBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(IForgeBlockState.class)
public interface BlockStateMixin {

    @Shadow(remap = false)
    default BlockState getBlockState() {
        return null;
    }

    /**
     * Add stormlight mod checks for slipperiness
     *
     * @author legobmw99
     * @reason unable to inject into default interface method
     */
    @Overwrite(remap = false)
    default float getSlipperiness(IWorldReader world, BlockPos pos, @Nullable Entity entity) {
        if ((entity instanceof PlayerEntity && ((PlayerEntity) entity).hasEffect(PowersSetup.SLICKING.get())) ||
            (entity instanceof BoatEntity && entity.hasPassenger(PlayerEntity.class) &&
             entity.getPassengers().stream().anyMatch(e -> e instanceof LivingEntity && ((LivingEntity) e).hasEffect(PowersSetup.SLICKING.get())))) {
            return 0.989f;
        }
        return getBlockState().getBlock().getSlipperiness(getBlockState(), world, pos, entity);

    }
}
