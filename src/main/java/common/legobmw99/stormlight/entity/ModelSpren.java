package common.legobmw99.stormlight.entity;

import net.minecraft.client.model.ModelShulkerBullet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.MathHelper;

public class ModelSpren extends ModelShulkerBullet{

	public ModelSpren(){
		super();
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
}
