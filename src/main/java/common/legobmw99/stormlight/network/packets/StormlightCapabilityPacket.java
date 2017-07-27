package common.legobmw99.stormlight.network.packets;

import common.legobmw99.stormlight.util.StormlightCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StormlightCapabilityPacket implements IMessage {

	public StormlightCapabilityPacket() {
	}

	private int type;
	private int progression;
	private int bladeStored;

	public StormlightCapabilityPacket(StormlightCapability c) {
		this.type = c.getType();
		this.progression = c.getProgression();
		this.bladeStored = c.isBladeStored() ? 1 : 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = ByteBufUtils.readVarInt(buf, 5);
		progression = ByteBufUtils.readVarInt(buf, 5);
		bladeStored = ByteBufUtils.readVarInt(buf, 5);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, type, 5);
		ByteBufUtils.writeVarInt(buf, progression, 5);
		ByteBufUtils.writeVarInt(buf, bladeStored, 5);
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
					if (cap != null) {
						cap.setType(message.type);
						cap.setProgression(message.progression);
						cap.setBladeStored(message.bladeStored == 1);
					}

				}
			});
			return null;
		}
	}
}
