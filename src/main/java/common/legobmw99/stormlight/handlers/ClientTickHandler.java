package common.legobmw99.stormlight.handlers;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.entity.EntitySpren;
import common.legobmw99.stormlight.items.ItemShardblade;
import common.legobmw99.stormlight.network.packets.BoundBladePacket;
import common.legobmw99.stormlight.network.packets.EffectEntityPacket;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
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
			GlStateManager.color(e.getRed(e.getType()), e.getGreen(e.getType()), e.getBlue(e.getType()), 0.5F);
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
			if (p.isPotionActive(Registry.effectStormlight)) {
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

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		EntityPlayerSP player;
		player = Minecraft.getMinecraft().player;
		if (player != null) {
			StormlightCapability cap = StormlightCapability.forPlayer(player);
			System.out.println(cap.getType());
			if (cap != null && cap.getType() >= 0
					&& cap.getProgression() > -2 /* Dummy check, for now */) {
				// Shardblade
				if (Registry.Recall.isPressed()) {
					Registry.network.sendToServer(new BoundBladePacket());
				}

				// Surges
				if (player.isPotionActive(Registry.effectStormlight)) {
					// Windrunners
					if (player.inventory
							.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.windrunners")))) {
						if (Registry.BindingOne.isPressed()) {
							if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
								Stormlight.surges.gravitation(player, 1);
							} else {
								Stormlight.surges.gravitation(player, 0);
							}
						}
						if (Registry.BindingTwo.isPressed()) {

						}

					}
					// Skybreakers
					if (player.inventory
							.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.skybreakers")))) {
						if (Registry.BindingOne.isPressed()) {

						}
						if (Registry.BindingTwo.isPressed()) {
							if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
								Stormlight.surges.gravitation(player, 1);
							} else {
								Stormlight.surges.gravitation(player, 0);
							}
						}

					}
					// Elsecallers
					if (player.inventory
							.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.elsecallers")))) {
						if (Registry.BindingOne.isPressed()) {
							if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
								Stormlight.surges.transportation(player, 1);
							} else {
								Stormlight.surges.transportation(player, 0);
							}
						}
						if (Registry.BindingTwo.isPressed()) {
							Stormlight.surges.transformation(player);
						}
					}
					// Edgedancers
					if (player.inventory
							.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.edgedancers")))) {
						if (Registry.BindingOne.isPressed()) {
							if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
								Stormlight.surges.progression(player, 1);
							} else {
								Stormlight.surges.progression(player, 0);
							}
						}
						if (Registry.BindingTwo.isPressed()) {

						}
					}
					// Truthwatchers
					if (player.inventory
							.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.truthwatchers")))) {
						if (Registry.BindingOne.isPressed()) {

						}
						if (Registry.BindingTwo.isPressed()) {
							if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
								Stormlight.surges.progression(player, 1);
							} else {
								Stormlight.surges.progression(player, 0);
							}
						}
					}
					// Lightweavers
					if (player.inventory
							.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.lightweavers")))) {
						if (Registry.BindingOne.isPressed()) {
							Stormlight.surges.transformation(player);
						}
						if (Registry.BindingTwo.isPressed()) {

						}
					}
					// Willshapers
					if (player.inventory
							.hasItemStack(new ItemStack(Item.getByNameOrId("stormlight:honorblade.willshapers")))) {
						if (Registry.BindingOne.isPressed()) {
						}
						if (Registry.BindingTwo.isPressed()) {
							if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
								Stormlight.surges.transportation(player, 1);
							} else {
								Stormlight.surges.transportation(player, 0);
							}
						}
					}
				}
				if (Registry.Reset.isPressed()) {
					Minecraft.getMinecraft().gameSettings.invertMouse = false;
					Minecraft.getMinecraft().entityRenderer.stopUseShader();
					Registry.network.sendToServer(
							new EffectEntityPacket(25, 1, 0, Minecraft.getMinecraft().player.getEntityId()));
				}
			}
		}
	}
}
