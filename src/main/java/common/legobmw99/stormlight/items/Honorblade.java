package common.legobmw99.stormlight.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemSword;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.util.Registry;

public class Honorblade extends ItemSword {
	
	public Honorblade(ToolMaterial material, String type, FMLPreInitializationEvent e) {
		super(material);
		setCreativeTab(Registry.tabStormlight);
		setRegistryName("honorblade." + type);
		setUnlocalizedName("honorblade." + type);
		GameRegistry.register(this);
		//Janky fix for rendering the way that I am creating multiple swords
		if(e.getSide() == Side.CLIENT){
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		}
	}
}
