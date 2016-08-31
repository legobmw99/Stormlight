package common.legobmw99.stormlight.util;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import common.legobmw99.stormlight.effects.effectStormlight;
import common.legobmw99.stormlight.items.Honorblade;
import common.legobmw99.stormlight.items.Sphere;
import common.legobmw99.stormlight.network.packets.BoundBladePacket;
import common.legobmw99.stormlight.network.packets.EffectEntityPacket;
import common.legobmw99.stormlight.network.packets.MoveEntityPacket;
import common.legobmw99.stormlight.network.packets.StopFallPacket;

public class Registry {
	
	public static void initItems(FMLPreInitializationEvent e){
		for (int meta = 0; meta < Bladetype.length; meta++) {
			itemBlade = new Honorblade(SHARD, Bladetype[meta], e);
		}
		for (int meta = 0; meta < Spheretype.length; meta++) {
			itemSphere = new Sphere(Spheretype[meta], e);
		}
	}
	
	public static void initRecipies(){
		GameRegistry.addShapedRecipe(new ItemStack(itemSphere, 8), new Object[] {
			"GGG", "GDG", "GGG", 'G', new ItemStack(Blocks.GLASS, 1), 'D', new ItemStack(Items.DIAMOND,1) });
		GameRegistry.addShapedRecipe(new ItemStack(itemSphere, 8), new Object[] {
			"GGG", "GDG", "GGG", 'G', new ItemStack(Blocks.GLASS, 1), 'D', new ItemStack(Items.EMERALD,1) });
	}
	
	public static void registerPackets(){
	       network = NetworkRegistry.INSTANCE.newSimpleChannel("stormlight");
	       network.registerMessage(StopFallPacket.Handler.class, StopFallPacket.class, 0, Side.SERVER);
	       network.registerMessage(MoveEntityPacket.Handler.class, MoveEntityPacket.class, 1, Side.SERVER);
	       network.registerMessage(EffectEntityPacket.Handler.class, EffectEntityPacket.class, 2, Side.SERVER);
	       network.registerMessage(BoundBladePacket.Handler.class, BoundBladePacket.class, 3, Side.SERVER);


	}
	
    @SideOnly(Side.CLIENT)
	public static void registerRenders(){
    	
	}
	public static void initKeybindings(){
		BindingOne = new KeyBinding("key.BindingOne", Keyboard.KEY_F, "key.categories.stormlight");
		BindingTwo = new KeyBinding("key.BindingTwo", Keyboard.KEY_G, "key.categories.stormlight");
		Reset = new KeyBinding("key.Reset", Keyboard.KEY_R, "key.categories.stormlight");
		Recall = new KeyBinding("key.Recall", Keyboard.KEY_V,"key.categories.stormlight");
        ClientRegistry.registerKeyBinding(BindingOne);
        ClientRegistry.registerKeyBinding(BindingTwo);
        ClientRegistry.registerKeyBinding(Reset);
        ClientRegistry.registerKeyBinding(Recall);
	}
	public static void registerEffect() {
		effectStormlight = (new effectStormlight(false, 0)).setPotionName("effect.stormlight");	

	}
	
	public static final ToolMaterial SHARD = EnumHelper.addToolMaterial("SHARD", 5, -1, 10.0F, 9.0F, 14);
	public static Honorblade itemBlade;
	public static final String[] Bladetype = {"windrunners","skybreakers","dustbringers","edgedancers","lightweavers","elsecallers","truthwaters","bondsmiths","willshapers","stonewards"};
	public static final String[] Spheretype = {"charged","dun"};
	public static Potion effectStormlight;
	public static Sphere itemSphere;
    public static KeyBinding BindingOne;
    public static KeyBinding BindingTwo;
    public static KeyBinding Reset;
    public static KeyBinding Recall;
    public static SimpleNetworkWrapper network;
	public static CreativeTabs tabStormlight = new CreativeTabStormlight(CreativeTabs.getNextID(), "Stormlight");

}
