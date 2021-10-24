package com.legobmw99.stormlight.modules.powers.mixin;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
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
@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockstateMixin {

    @Shadow(remap = false)
    public abstract Block getBlock();

    @Shadow(remap = false)
    protected abstract BlockState asState();

    @Inject(at = @At("HEAD"), method = "getCollisionShape(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/shapes/ISelectionContext;)Lnet/minecraft/util/math/shapes/VoxelShape;", cancellable = true, remap = false)
    private void noClip(IBlockReader world, BlockPos pos, ISelectionContext context, CallbackInfoReturnable<VoxelShape> info) {
        Entity entity = context.getEntity();
        if (entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(PowersSetup.COHESION.get())) {
            boolean isAbove = isAbove(entity, getBlock().getCollisionShape(asState(), world, pos, context), pos);
            if (getBlock() != Blocks.BEDROCK && (!isAbove || entity.isShiftKeyDown())) {
                info.setReturnValue(VoxelShapes.empty());
            }
        }
    }

    @Unique
    private boolean isAbove(Entity entity, VoxelShape shape, BlockPos pos) {
        return entity.getY() > (double) pos.getY() + shape.max(Direction.Axis.Y) - (entity.isOnGround() ? 8.05 / 16.0 : 0.0015);
    }

    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true, remap = false)
    private void preventPushOut(World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(PowersSetup.COHESION.get())) {
            ci.cancel();
        }
    }

}


