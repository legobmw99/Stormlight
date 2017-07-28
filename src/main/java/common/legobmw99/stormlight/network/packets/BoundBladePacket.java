
package common.legobmw99.stormlight.network.packets;

import common.legobmw99.stormlight.items.ItemShardblade;
import common.legobmw99.stormlight.util.Registry;
import common.legobmw99.stormlight.util.StormlightCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BoundBladePacket implements IMessage {
	public BoundBladePacket() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<BoundBladePacket, IMessage> {

		@Override
		public IMessage onMessage(final BoundBladePacket message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					EntityPlayerMP player = ctx.getServerHandler().player;
					if (player != null) {
						StormlightCapability cap = StormlightCapability.forPlayer(player);
						if (cap != null) {
							if (cap.getType() >= 0 && cap.isBladeStored()) {
								if(player.inventory.addItemStackToInventory(new ItemStack(Registry.itemBlade, 1, cap.getType()))){
									cap.setBladeStored(false);
								}
							} else if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemShardblade && player.getHeldItemMainhand().getItemDamage() == cap.getType()){
								player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.AIR, 0));
								cap.setBladeStored(true);
							}
						}
					}
				}
			});
			return null;
		}
	}
}