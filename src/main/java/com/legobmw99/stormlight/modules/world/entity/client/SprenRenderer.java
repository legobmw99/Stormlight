package com.legobmw99.stormlight.modules.world.entity.client;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.world.entity.SprenEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class SprenRenderer extends MobRenderer<SprenEntity, SprenModel> {


    public SprenRenderer(EntityRendererManager manager) {
        super(manager, new SprenModel(), 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(SprenEntity entity) {
        return new ResourceLocation(Stormlight.MODID, "textures/entity/spren.png");
    }
}
