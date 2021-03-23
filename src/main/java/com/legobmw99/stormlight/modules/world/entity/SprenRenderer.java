package com.legobmw99.stormlight.modules.world.entity;

import com.legobmw99.stormlight.Stormlight;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class SprenRenderer extends LivingRenderer<SprenEntity,SprenModel> {


    public SprenRenderer(EntityRendererManager p_i50965_1_, SprenModel p_i50965_2_) {
        super(p_i50965_1_, p_i50965_2_, 0.0f);
    }


    @Override
    public ResourceLocation getTextureLocation(SprenEntity p_110775_1_) {
        return new ResourceLocation(Stormlight.MODID, "textures/entity/spren.png");
    }
}
