package common.legobmw99.stormlight.items;

import java.util.List;

import javax.annotation.Nullable;

import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.util.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Shardblade extends ItemSword {

	public Shardblade(ToolMaterial material) {
		super(material);
		setCreativeTab(Registry.tabStormlight);
		setRegistryName(new ResourceLocation(Stormlight.MODID,"shardblade"));
		setNoRepair();
		setMaxDamage(-1);
		setHasSubtypes(true);

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
	
	@SideOnly(Side.CLIENT)
	public void initModel(){
		ModelBakery.registerItemVariants(this,
				new ModelResourceLocation(getRegistryName() + ".windrunners", "inventory"),
				new ModelResourceLocation(getRegistryName() + ".skybreakers", "inventory"),
				new ModelResourceLocation(getRegistryName() + ".dustbringers", "inventory"),
				new ModelResourceLocation(getRegistryName() + ".edgedancers", "inventory"),
				new ModelResourceLocation(getRegistryName() + ".lightweavers", "inventory"),
				new ModelResourceLocation(getRegistryName() + ".elsecallers", "inventory"),
				new ModelResourceLocation(getRegistryName() + ".truthwatchers", "inventory"),
				new ModelResourceLocation(getRegistryName() + ".bondsmiths", "inventory"),
				new ModelResourceLocation(getRegistryName() + ".willshapers", "inventory"),
				new ModelResourceLocation(getRegistryName() + ".stonewards", "inventory"));
		
		for (int i = 0; i < Registry.BLADE_TYPES.length; i++) {        
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, i,
					new ModelResourceLocation(getRegistryName() + "." + Registry.BLADE_TYPES[i], "inventory"));
		}
	}
	
	
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int meta = itemStack.getItemDamage();
        if ((meta < 0) || (meta >= Registry.BLADE_TYPES.length)) {
            meta = 0;
        }
        return "item.shardblade" + "." + Registry.BLADE_TYPES[meta];
    }

    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.RARE;
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(tab)){ 
        	for (int meta = 0; meta < Registry.BLADE_TYPES.length; meta++) {
        		subItems.add(new ItemStack(this, 1, meta));
        	}
        }
    }
}
