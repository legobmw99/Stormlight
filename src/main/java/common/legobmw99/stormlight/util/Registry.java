package common.legobmw99.stormlight.util;

import org.lwjgl.input.Keyboard;

import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.effects.effectStormlight;
import common.legobmw99.stormlight.entity.RenderSpren;
import common.legobmw99.stormlight.entity.EntitySpren;
import common.legobmw99.stormlight.items.Honorblade;
import common.legobmw99.stormlight.items.Sphere;
import common.legobmw99.stormlight.network.packets.BoundBladePacket;
import common.legobmw99.stormlight.network.packets.EffectEntityPacket;
import common.legobmw99.stormlight.network.packets.GrowPacket;
import common.legobmw99.stormlight.network.packets.MoveEntityPacket;
import common.legobmw99.stormlight.network.packets.StopFallPacket;
import common.legobmw99.stormlight.network.packets.TeleportPlayerPacket;
import common.legobmw99.stormlight.network.packets.TransformBlockPacket;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Registry {
	
	public static void initItems(Register e){
		for (int meta = 0; meta < Bladetype.length; meta++) {
			e.getRegistry().register(itemBlade = new Honorblade(SHARD, Bladetype[meta], e));
		}
		for (int meta = 0; meta < Spheretype.length; meta++) {
			e.getRegistry().register(itemSphere = new Sphere(Spheretype[meta], e));
		}
	}
	
	public static void registerPackets(){
	       network = NetworkRegistry.INSTANCE.newSimpleChannel("stormlight");
	       network.registerMessage(StopFallPacket.Handler.class, StopFallPacket.class, 0, Side.SERVER);
	       network.registerMessage(MoveEntityPacket.Handler.class, MoveEntityPacket.class, 1, Side.SERVER);
	       network.registerMessage(EffectEntityPacket.Handler.class, EffectEntityPacket.class, 2, Side.SERVER);
	       network.registerMessage(BoundBladePacket.Handler.class, BoundBladePacket.class, 3, Side.SERVER);
	       network.registerMessage(TeleportPlayerPacket.Handler.class, TeleportPlayerPacket.class, 4, Side.SERVER);
	       network.registerMessage(TransformBlockPacket.Handler.class, TransformBlockPacket.class, 5, Side.SERVER);
	       network.registerMessage(GrowPacket.Handler.class, GrowPacket.class, 6, Side.SERVER);



	}
	
	public static void registerEntities(){
		int id = 1;
        EntityRegistry.registerModEntity(new ResourceLocation(Stormlight.MODID, "Spren"), EntitySpren.class, "Spren", id++, Stormlight.instance, 64, 3, true, 0x996600, 0x00ff00);
        EntityRegistry.addSpawn(EntitySpren.class, 25, 1, 1, EnumCreatureType.CREATURE, Biomes.PLAINS, Biomes.ICE_PLAINS, Biomes.TAIGA, Biomes.FOREST, Biomes.DESERT,Biomes.JUNGLE,Biomes.MESA,Biomes.SAVANNA,Biomes.EXTREME_HILLS,Biomes.SWAMPLAND);

	}
	
    @SideOnly(Side.CLIENT)
	public static void registerRenders(){
        RenderingRegistry.registerEntityRenderingHandler(EntitySpren.class, RenderSpren.FACTORY);
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
	
	public static void registerEffect(Register event) {
		event.getRegistry().register(effectStormlight = (new effectStormlight(false, 0)).setPotionName("effect.stormlight"));	

	}
	
	public static final ToolMaterial SHARD = EnumHelper.addToolMaterial("SHARD", 5, -1, 10.0F, 9.0F, 14);
	public static Honorblade itemBlade;
	public static final String[] Bladetype = {"windrunners","skybreakers","dustbringers","edgedancers","lightweavers","elsecallers","truthwatchers","bondsmiths","willshapers","stonewards"};
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
