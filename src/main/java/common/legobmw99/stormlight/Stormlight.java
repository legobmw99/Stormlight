package common.legobmw99.stormlight;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.potion.Potion;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import common.legobmw99.stormlight.handlers.StormlightTickHandler;
import common.legobmw99.stormlight.util.Registry;
import common.legobmw99.stormlight.util.Surges;

@Mod(modid = Stormlight.MODID, version = Stormlight.VERSION)
public class Stormlight {
	public static final String MODID = "stormlight";
	public static final String VERSION ="@version@";
	public static Surges surges;
	
	 @SidedProxy
	    public static CommonProxy proxy;

	    @Mod.Instance
	    public static Stormlight instance;


	    @Mod.EventHandler
	    public void preInit(FMLPreInitializationEvent event){
	        proxy.preInit(event);
	    }

	    @Mod.EventHandler
	    public void init(FMLInitializationEvent e) {
	        proxy.init(e);
	    }

	    @Mod.EventHandler
	    public void postInit(FMLPostInitializationEvent e) {
	        proxy.postInit(e);
	    }

	    
	    public static class CommonProxy {
	        public void preInit(FMLPreInitializationEvent e) {
	        	Registry.initItems(e);
	        	Registry.registerPackets();
	        	
	        	Potion[] potionTypes = null;

	    		for (Field f : Potion.class.getDeclaredFields()) {
	    			f.setAccessible(true);
	    			try {
	    				if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) {
	    					Field modfield = Field.class.getDeclaredField("modifiers");
	    					modfield.setAccessible(true);
	    					modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

	    					potionTypes = (Potion[])f.get(null);
	    					final Potion[] newPotionTypes = new Potion[256];
	    					System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
	    					f.set(null, newPotionTypes);
	    				}
	    			}
	    			catch (Exception e1) {
	    				System.err.println("Severe error, please report this to the mod author:");
	    				System.err.println(e1);
	    			}
	    		}
	        }

	        public void init(FMLInitializationEvent e) {
	    		MinecraftForge.EVENT_BUS.register(new StormlightTickHandler());
	    		Registry.registerEffect();
	    		Registry.initRecipies();
	        }

	        public void postInit(FMLPostInitializationEvent e) {

	        }
	    }


	    public static class ClientProxy extends CommonProxy {
	        @Override
	        public void preInit(FMLPreInitializationEvent e) {
	            super.preInit(e);

	            OBJLoader.INSTANCE.addDomain(MODID);
	            // Typically initialization of models and such goes here:
	            Registry.registerRenders();
	            Registry.initKeybindings();
	            Stormlight.surges = new Surges();
	        }

	        @Override
	        public void init(FMLInitializationEvent e) {
	            super.init(e);

	            // Initialize our input handler so we can listen to keys
	        }
	    }

	    public static class ServerProxy extends CommonProxy {

	    }
}

