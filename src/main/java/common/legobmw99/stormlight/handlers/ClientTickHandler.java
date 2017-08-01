package common.legobmw99.stormlight.handlers;

import java.util.List;

import org.lwjgl.opengl.GL11;

import common.legobmw99.stormlight.entity.EntitySpren;
import common.legobmw99.stormlight.network.packets.BoundBladePacket;
import common.legobmw99.stormlight.network.packets.SurgeFiredPacket;
import common.legobmw99.stormlight.util.Registry;
import common.legobmw99.stormlight.util.StormlightCapability;
import elucent.albedo.event.GatherLightsEvent;
import elucent.albedo.lighting.Light;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientTickHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onEntityRenderPre(RenderLivingEvent.Pre event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntitySpren) {
			EntitySpren e = (EntitySpren) entity;
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.enableNormalize();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(e.getRed(), e.getGreen(), e.getBlue(), 0.5F);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onEntityRenderPost(RenderLivingEvent.Post event) {
		if (event.getEntity() instanceof EntitySpren) {
			GlStateManager.disableBlend();
			GlStateManager.disableNormalize();
			GlStateManager.popMatrix();
		}
	}

	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = "albedo")
	@SubscribeEvent
	public void onLightGather(GatherLightsEvent event) {
		List<EntityPlayer> pList = Minecraft.getMinecraft().player.world.playerEntities;
		for (EntityPlayer p : pList) {
			if (p.isPotionActive(Registry.effectStormlight) && !p.isPotionActive(Potion.getPotionById(14))) {
				event.getLightList().add(
						Light.builder().pos(p.posX, p.posY + 1.0, p.posZ).radius(4.5F).color(0.4F, 0.6F, 1.0F).build());
			}
		}
		List<Entity> eList = Minecraft.getMinecraft().player.world.getLoadedEntityList();
		for (Entity e : eList) {
			if (e instanceof EntityItem) {
				if (((EntityItem) e).getItem().isItemEqual(new ItemStack(Registry.itemSphere, 1, 1))) {
					event.getLightList().add(Light.builder().pos(e).radius(2F).color(0.4F, 0.6F, 1.0F).build());
				}
			} else if (e instanceof EntityItemFrame) {
				if (((EntityItemFrame) e).getDisplayedItem().isItemEqual(new ItemStack(Registry.itemSphere, 1, 1))) {
					event.getLightList().add(Light.builder().pos(e).radius(2F).color(0.4F, 0.6F, 1.0F).build());
				}
			}
		}
	}
	
	//Slow down how often the Surges repeat-fire
	boolean shouldFire = false;
	@SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        // Run once per tick, only if in game, and only if there is a player
        if (event.phase == TickEvent.Phase.END && (!Minecraft.getMinecraft().isGamePaused() && Minecraft.getMinecraft().player != null)) {
        	if(!shouldFire){
        		shouldFire = true;
        		return;
        	}
        	EntityPlayerSP player;
    		player = Minecraft.getMinecraft().player;
    		if (player != null) {
    			StormlightCapability cap = StormlightCapability.forPlayer(player);
    			if (cap != null) {

    				if (cap.getProgression() > -1 /* Dummy check, for now */) {
    					// Shardblade recall
    					if (Registry.Recall.isPressed()) {
    						Registry.network.sendToServer(new BoundBladePacket());
    					}
    				}
    				
    				if (cap.getProgression() > -1 /* Dummy check, for now */) {
    					// Surges
    					if (player.isPotionActive(Registry.effectStormlight)) {
    						if (Registry.BindingOne.isKeyDown()) {
    							RayTraceResult ray = player.rayTrace(20.0F, 0.0F);
    							Registry.network.sendToServer(new SurgeFiredPacket(0,
    									Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown(), ray.getBlockPos())); //All serverside surges
    						}
    						
    						if (Registry.BindingTwo.isKeyDown()) {
    							RayTraceResult ray = player.rayTrace(20.0F, 0.0F);
    							Registry.network.sendToServer(new SurgeFiredPacket(1,
    									Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown(), ray.getBlockPos())); //All serverside surges
							
    						}
       					}
    				}
    			}
    		}
    		shouldFire = false;
    	}
	}


	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		
		/* TODO: effect for gravitation?
		 * Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/flip.json"));
		 * Minecraft.getMinecraft().gameSettings.invertMouse = true; 
		 * 
		 * and
		 * 
		 * Minecraft.getMinecraft().gameSettings.invertMouse = false;
		 * Minecraft.getMinecraft().entityRenderer.stopUseShader();
		 */
	}
}
