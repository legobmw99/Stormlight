package common.legobmw99.stormlight.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
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
		return new ItemStack(Items.DIAMOND_HOE, 1);
	}
	@Override
	public Item getTabIconItem() {
		// TODO Auto-generated method stub
		return null;
	}

}
