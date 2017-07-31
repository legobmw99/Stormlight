package common.legobmw99.stormlight.network.packets;

import common.legobmw99.stormlight.util.StormlightCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StormlightCapabilityPacket implements IMessage {

	public StormlightCapabilityPacket() {
	}

	private NBTTagCompound data;

	public StormlightCapabilityPacket(StormlightCapability c) {
		this.data = c.serializeNBT();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		data = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
	}

	public static class Handler implements IMessageHandler<StormlightCapabilityPacket, IMessage> {

		@Override
		public IMessage onMessage(final StormlightCapabilityPacket message, final MessageContext ctx) {
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					EntityPlayer player = Minecraft.getMinecraft().player;
					StormlightCapability cap = StormlightCapability.forPlayer(player);
					cap.deserializeNBT(message.data);

				}
			});
			return null;
		}
	}
}
