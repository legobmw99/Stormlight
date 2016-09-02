package common.legobmw99.stormlight.util;

import java.util.concurrent.ThreadLocalRandom;

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
		return new ItemStack(Item.getByNameOrId("stormlight:honorblade.windrunners"));
	}
	@Override
	public Item getTabIconItem() {
		return null;
	}

}
