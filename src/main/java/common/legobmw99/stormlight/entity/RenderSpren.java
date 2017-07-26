package common.legobmw99.stormlight.entity;

import common.legobmw99.stormlight.Stormlight;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderSpren extends RenderLiving<EntitySpren> {

    private ResourceLocation mobTexture = new ResourceLocation(Stormlight.MODID,"textures/entity/spren.png");

	public RenderSpren(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelSpren(), 0.0F);
	}
	
    public static final Factory FACTORY = new Factory();


	@Override
	protected ResourceLocation getEntityTexture(EntitySpren entity) {
		
		return mobTexture;
	}


    public static class Factory implements IRenderFactory<EntitySpren> {

        @Override
        public Render<? super EntitySpren> createRenderFor(RenderManager manager) {
            return new RenderSpren(manager);
        }

    }
}
