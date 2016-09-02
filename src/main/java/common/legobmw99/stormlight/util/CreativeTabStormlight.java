package common.legobmw99.stormlight.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabStormlight extends CreativeTabs {

	public CreativeTabStormlight(int index, String label) {
		super(index, label);
	}


	@Override
	public String getTabLabel() {
		return "Stormlight";
	}
	@Override
	public ItemStack getIconItemStack() {
		//Make the icon a different honorblade every minute
		int type = (int)(System.currentTimeMillis()/(1000*60)) % 10;
		return new ItemStack(Item.getByNameOrId("stormlight:honorblade." + Registry.Bladetype[type]));
	}
	@Override
	public Item getTabIconItem() {
		return null;
	}

}
