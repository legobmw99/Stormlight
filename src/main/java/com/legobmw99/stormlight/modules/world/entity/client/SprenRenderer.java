package com.legobmw99.stormlight.modules.world.entity.client;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.world.entity.SprenEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class SprenRenderer extends MobRenderer<SprenEntity, SprenModel> {


    public SprenRenderer(EntityRendererProvider.Context manager) {
        super(manager, new SprenModel(manager.bakeLayer(SprenModel.createLayer())), 0.0f);
    }

    @Override
    protected int getBlockLightLevel(@Nonnull SprenEntity e, @Nonnull BlockPos p) {
        return 15;
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull SprenEntity entity) {
        return new ResourceLocation(Stormlight.MODID, "textures/entity/spren.png");
    }
}
