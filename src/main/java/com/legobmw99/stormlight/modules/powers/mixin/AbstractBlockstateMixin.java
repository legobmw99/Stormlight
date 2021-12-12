package com.legobmw99.stormlight.modules.powers.mixin;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Primarily handles the Cohesion effect of phasing through blocks. Inspired by Origins
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockstateMixin {

    @Shadow(remap = false)
    public abstract Block getBlock();

    @Shadow(remap = false)
    protected abstract BlockState asState();

    @Inject(at = @At("HEAD"), method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", cancellable = true, remap = false)
    private void noClip(BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> info) {
        if (context instanceof EntityCollisionContext ectx) {
            Entity entity = ectx.getEntity();
            if (entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(PowersSetup.COHESION.get())) {
                boolean isAbove = isAbove(entity, getBlock().getCollisionShape(asState(), world, pos, context), pos);
                if (getBlock() != Blocks.BEDROCK && (!isAbove || entity.isShiftKeyDown())) {
                    info.setReturnValue(Shapes.empty());
                }
            }

        }

    }

    @Unique
    private boolean isAbove(Entity entity, VoxelShape shape, BlockPos pos) {
        return entity.getY() > (double) pos.getY() + shape.max(Direction.Axis.Y) - (entity.isOnGround() ? 8.05 / 16.0 : 0.0015);
    }

    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true, remap = false)
    private void preventPushOut(Level world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(PowersSetup.COHESION.get())) {
            ci.cancel();
        }
    }

}


