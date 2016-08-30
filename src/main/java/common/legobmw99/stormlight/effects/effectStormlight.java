package common.legobmw99.stormlight.effects;

import common.legobmw99.stormlight.Stormlight;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class effectStormlight extends Potion {

	public effectStormlight(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn);
		setBeneficial();
		this.setIconIndex(0,0);
		GameRegistry.register(this, new ResourceLocation("stormlight"));
	}
	

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasStatusIcon() 
	{
	    Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Stormlight.MODID, "/textures/gui/effect.png"));
		this.setIconIndex(0,0);
	    return true;
	}
	

}
