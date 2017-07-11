package common.legobmw99.stormlight.items;

import java.util.List;

import javax.annotation.Nullable;

import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.util.Registry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;

public class Honorblade extends ItemSword {

	public Honorblade(ToolMaterial material, String type, Register e) {
		super(material);
		setCreativeTab(Registry.tabStormlight);
		setRegistryName(new ResourceLocation(Stormlight.MODID,"honorblade." + type));
		setUnlocalizedName("honorblade." + type);
		// Janky fix for rendering the way that I am creating multiple swords
		//this is literally so awful
		try{
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		} catch (NoClassDefFoundError ex){
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("\u00A7bThis sword seems to glow with strange light");
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		// Add enchantment glint
		return true;
	}

}
