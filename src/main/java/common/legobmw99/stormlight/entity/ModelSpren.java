package common.legobmw99.stormlight.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShulkerBullet;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.MathHelper;

public class ModelSpren extends ModelBase{
	
    public ModelRenderer renderer;
    
	public ModelSpren(){
		this.textureWidth = 32;
		this.textureHeight = 16;
        this.renderer = new ModelRenderer(this, 0, 0);
        this.renderer.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.renderer.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.renderer.offsetY = 0.5F;
	}
	
	@Override
	 public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
		EntitySpren spren = (EntitySpren)entitylivingbaseIn;


        if (spren.isSitting())
        {
            this.renderer.offsetY = 1.0F;
        }
        else
        {
            this.renderer.offsetY = 0.5F;
        }

    }
	
	@Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.renderer.render(scale);

    }

	@Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.renderer.rotateAngleY = netHeadYaw * 0.017453292F;
        this.renderer.rotateAngleX = headPitch * 0.017453292F;
    }
}
