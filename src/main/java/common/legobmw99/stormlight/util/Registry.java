package common.legobmw99.stormlight.util;

import org.lwjgl.input.Keyboard;

import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.effects.effectStormlight;
import common.legobmw99.stormlight.entity.EntitySpren;
import common.legobmw99.stormlight.entity.RenderSpren;
import common.legobmw99.stormlight.items.ItemShardblade;
import common.legobmw99.stormlight.items.ItemSphere;
import common.legobmw99.stormlight.network.packets.BoundBladePacket;
import common.legobmw99.stormlight.network.packets.EffectEntityPacket;
import common.legobmw99.stormlight.network.packets.GrowPacket;
import common.legobmw99.stormlight.network.packets.MoveEntityPacket;
import common.legobmw99.stormlight.network.packets.StopFallPacket;
import common.legobmw99.stormlight.network.packets.StormlightCapabilityPacket;
import common.legobmw99.stormlight.network.packets.TeleportPlayerPacket;
import common.legobmw99.stormlight.network.packets.TransformBlockPacket;
import net.minecraft.client.Minecraft;
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

	public static void initItems(Register e) {
		e.getRegistry().registerAll(
				itemBlade = new ItemShardblade(SHARD), 
				itemSphere = new ItemSphere());
	}

	public static void registerPackets() {
		network = NetworkRegistry.INSTANCE.newSimpleChannel("stormlight");
		network.registerMessage(StopFallPacket.Handler.class, StopFallPacket.class, 0, Side.SERVER);
		network.registerMessage(MoveEntityPacket.Handler.class, MoveEntityPacket.class, 1, Side.SERVER);
		network.registerMessage(EffectEntityPacket.Handler.class, EffectEntityPacket.class, 2, Side.SERVER);
		network.registerMessage(BoundBladePacket.Handler.class, BoundBladePacket.class, 3, Side.SERVER);
		network.registerMessage(TeleportPlayerPacket.Handler.class, TeleportPlayerPacket.class, 4, Side.SERVER);
		network.registerMessage(TransformBlockPacket.Handler.class, TransformBlockPacket.class, 5, Side.SERVER);
		network.registerMessage(GrowPacket.Handler.class, GrowPacket.class, 6, Side.SERVER);
		network.registerMessage(StormlightCapabilityPacket.Handler.class, StormlightCapabilityPacket.class, 7, Side.CLIENT);

	}

	public static void registerEntities() {
		int id = 1;
		EntityRegistry.registerModEntity(new ResourceLocation(Stormlight.MODID, "Spren"), EntitySpren.class, "Spren",
				id++, Stormlight.instance, 64, 3, true, 0xEDE2FF, 0xC698CE);
		
		EntityRegistry.addSpawn(EntitySpren.class, 25, 1, 1, EnumCreatureType.CREATURE, Biomes.PLAINS,
				Biomes.ICE_PLAINS, Biomes.TAIGA, Biomes.FOREST, Biomes.DESERT, Biomes.JUNGLE, Biomes.MESA,
				Biomes.SAVANNA, Biomes.EXTREME_HILLS, Biomes.SWAMPLAND, Biomes.MUSHROOM_ISLAND);
	}

	@SideOnly(Side.CLIENT)
	public static void registerEntityRenders(){
		RenderingRegistry.registerEntityRenderingHandler(EntitySpren.class, RenderSpren.FACTORY);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerItemRenders() {
		itemBlade.initModel();
		itemSphere.initModel();
		
		//TODO: investigate other solutions to the variants-not-loading-initially problem
		Minecraft.getMinecraft().refreshResources();
	}

	public static void initKeybindings() {
		BindingOne = new KeyBinding("key.BindingOne", Keyboard.KEY_F, "key.categories.stormlight");
		BindingTwo = new KeyBinding("key.BindingTwo", Keyboard.KEY_G, "key.categories.stormlight");
		Reset = new KeyBinding("key.Reset", Keyboard.KEY_R, "key.categories.stormlight");
		Recall = new KeyBinding("key.Recall", Keyboard.KEY_V, "key.categories.stormlight");
		ClientRegistry.registerKeyBinding(BindingOne);
		ClientRegistry.registerKeyBinding(BindingTwo);
		ClientRegistry.registerKeyBinding(Reset);
		ClientRegistry.registerKeyBinding(Recall);
	}

	public static void registerEffect(Register event) {
		event.getRegistry()
				.register(effectStormlight = (new effectStormlight(false, 0)).setPotionName("effect.stormlight"));
	}

	public static final ToolMaterial SHARD = EnumHelper.addToolMaterial("SHARD", 5, -1, 10.0F, 9.0F, 14);
	public static final String[] BLADE_TYPES = { "windrunners", "skybreakers", "dustbringers", "edgedancers","lightweavers", "elsecallers", "truthwatchers", "bondsmiths", "willshapers", "stonewards" };
	public static final String[] SPHERE_TYPES = { "dun", "charged"};
	public static Potion effectStormlight;
	public static ItemSphere itemSphere;
	public static ItemShardblade itemBlade;
	public static KeyBinding BindingOne;
	public static KeyBinding BindingTwo;
	public static KeyBinding Reset;
	public static KeyBinding Recall;
	public static SimpleNetworkWrapper network;
	public static CreativeTabs tabStormlight = new CreativeTabStormlight(CreativeTabs.getNextID(), "Stormlight");

}
