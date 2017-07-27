package common.legobmw99.stormlight.items;

import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.util.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSphere extends Item {
	public ItemSphere() {
		super();
		setCreativeTab(Registry.tabStormlight);
		setRegistryName(new ResourceLocation(Stormlight.MODID, "sphere"));
		setMaxStackSize(16);
		setHasSubtypes(true);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.NONE;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 5;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (playerIn.getHeldItem(hand).getItemDamage() == 1) {
			playerIn.setActiveHand(hand);
			return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
		} else {
			return new ActionResult(EnumActionResult.FAIL, playerIn.getHeldItem(hand));
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		// Don't consume items in creative mode
		if (entityLiving != null && entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			if (player.capabilities.isCreativeMode != true) {
				stack.shrink(1);
				player.inventory.addItemStackToInventory(new ItemStack(this, 1, 0));
			}
		}
		if (!worldIn.isRemote) {
			if(entityLiving.isPotionActive(Registry.effectStormlight)){
				entityLiving.addPotionEffect(new PotionEffect(Registry.effectStormlight, Math.min(4800+ entityLiving.getActivePotionEffect(Registry.effectStormlight).getDuration(), 19200)));
			} else {
				entityLiving.addPotionEffect(new PotionEffect(Registry.effectStormlight, 4800));
			}
		}
		return stack;
	}
	
    @Override
    public EnumRarity getRarity(ItemStack stack){
        return stack.getItemDamage() == 1 ? EnumRarity.RARE : EnumRarity.COMMON;
    }
    
	@SideOnly(Side.CLIENT)
	public void initModel(){
		ModelBakery.registerItemVariants(this,
				new ModelResourceLocation(getRegistryName() + ".dun", "inventory"),
				new ModelResourceLocation(getRegistryName() + ".charged", "inventory"));
		
		for (int i = 0; i < Registry.SPHERE_TYPES.length; i++) {        
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
			.register(this, i, new ModelResourceLocation(getRegistryName() +"." + Registry.SPHERE_TYPES[i], "inventory"));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if ((meta < 0) || (meta >= Registry.SPHERE_TYPES.length)) {
			meta = 0;
		}
		return "item.sphere" + "." + Registry.SPHERE_TYPES[meta];
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (isInCreativeTab(tab)) {
			for (int meta = 0; meta < Registry.SPHERE_TYPES.length; meta++) {
				subItems.add(new ItemStack(this, 1, meta));
			}
		}
	}
}
