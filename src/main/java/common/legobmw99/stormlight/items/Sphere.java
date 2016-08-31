package common.legobmw99.stormlight.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLiving;
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
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import common.legobmw99.stormlight.util.Registry;

public class Sphere extends Item{
	public Sphere(String type, FMLPreInitializationEvent e){
		super();
		setCreativeTab(Registry.tabStormlight);
		setRegistryName("sphere." + type);
		setUnlocalizedName("sphere." + type);
		setMaxStackSize(16);
		GameRegistry.register(this);
		if(e.getSide() == Side.CLIENT){
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		}
	}
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.DRINK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 10;
	}
	
	@Override
	   public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	    {
		if(itemStackIn.isItemEqual(new ItemStack(Item.getByNameOrId("Stormlight:sphere.charged")))){
	        playerIn.setActiveHand(hand);
	        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		} else {
	        return new ActionResult(EnumActionResult.FAIL, itemStackIn);		
			}
	    }

	
	@Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		//Don't consume items in creative mode
		if(entityLiving != null && entityLiving instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) entityLiving;
		if (player.capabilities.isCreativeMode != true) {
			stack.stackSize--; 
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
