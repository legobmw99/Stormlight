package common.legobmw99.stormlight.items;

import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.util.Registry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;

public class Sphere extends Item{
	public Sphere(String type, Register e){
		super();
		setCreativeTab(Registry.tabStormlight);
		setRegistryName(new ResourceLocation(Stormlight.MODID,"sphere." + type));
		setUnlocalizedName("sphere." + type);
		setMaxStackSize(16);
		
		//this is literally so awful
		try{
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		} catch (NoClassDefFoundError ex){
		}
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
	   public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	    {
		if(playerIn.getHeldItem(hand).isItemEqual(new ItemStack(Item.getByNameOrId("Stormlight:sphere.charged")))){
	        playerIn.setActiveHand(hand);
	        return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
		} else {
	        return new ActionResult(EnumActionResult.FAIL, playerIn.getHeldItem(hand));		
			}
	    }
	
	
	@Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		//Don't consume items in creative mode
		if(entityLiving != null && entityLiving instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) entityLiving;
		if (player.capabilities.isCreativeMode != true) {
			stack.shrink(1);; 
			player.inventory.addItemStackToInventory(new ItemStack(Registry.itemSphere, 1,0));
		}
		}
		 if (!worldIn.isRemote)
	        {
             entityLiving.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("stormlight:effect.stormlight"), 4800));
	        }

		return stack;
	}
}
