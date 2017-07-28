package common.legobmw99.stormlight.network.packets;

import common.legobmw99.stormlight.util.StormlightCapability;
import common.legobmw99.stormlight.util.Surges;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SurgeFiredPacket implements IMessage {

	private static final int FIRST = 0;
	private static final int SECOND = 1;

	private int surgeUsed;
	private int shiftHeld;
	private int x, y, z;

	public SurgeFiredPacket() {
	}

	public SurgeFiredPacket(int used, boolean shift, int x, int y, int z) {
		surgeUsed = used;
		shiftHeld = shift ? 1 : 0;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		surgeUsed = ByteBufUtils.readVarInt(buf, 5);
		shiftHeld = ByteBufUtils.readVarInt(buf, 5);
		x = ByteBufUtils.readVarInt(buf, 5);
		y = ByteBufUtils.readVarInt(buf, 5);
		z = ByteBufUtils.readVarInt(buf, 5);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, surgeUsed, 5);
		ByteBufUtils.writeVarInt(buf, shiftHeld, 5);
		ByteBufUtils.writeVarInt(buf, x, 5);
		ByteBufUtils.writeVarInt(buf, y, 5);
		ByteBufUtils.writeVarInt(buf, z, 5);
	}

	public static class Handler implements IMessageHandler<SurgeFiredPacket, IMessage> {

		@Override
		public IMessage onMessage(final SurgeFiredPacket message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					EntityPlayerMP player = ctx.getServerHandler().player;
					StormlightCapability cap = StormlightCapability.forPlayer(player);
					boolean shiftHeld = message.shiftHeld == 1;
					BlockPos pos = new BlockPos(message.x, message.y, message.z);

					if (cap != null && /*Dummy check for now*/ cap.getProgression() > -2) {
						switch (cap.getType()) {
						case StormlightCapability.WINDRUNNERS:
							if (message.surgeUsed == FIRST) {
								Surges.gravitation(player, shiftHeld);
							} else if (message.surgeUsed == SECOND) {
								Surges.adhesion(player.getEntityWorld(), pos, shiftHeld);
							}
							
							break;
						case StormlightCapability.SKYBREAKERS:
							if (message.surgeUsed == FIRST) {
								Surges.division(player, pos, shiftHeld);
							} else if (message.surgeUsed == SECOND) {
								Surges.gravitation(player, shiftHeld);
							}
							
							break;
						case StormlightCapability.DUSTBRINGERS:
							if (message.surgeUsed == FIRST) {
								Surges.abrasion(player, shiftHeld);
							} else if (message.surgeUsed == SECOND) {
								Surges.division(player, pos, shiftHeld);
							}
							
							break;
						case StormlightCapability.EDGEDANCERS:
							if (message.surgeUsed == FIRST) {
								Surges.progression(player, pos, shiftHeld);
							} else if (message.surgeUsed == SECOND) {
								Surges.abrasion(player, shiftHeld);
							}
							
							break;
						case StormlightCapability.TRUTHWATCHERS:
							if (message.surgeUsed == FIRST) {
								Surges.illumination(player, pos, shiftHeld);
							} else if (message.surgeUsed == SECOND) {
								Surges.progression(player, pos, shiftHeld);
							}
							
							break;
						case StormlightCapability.LIGHTWEAVERS:
							if (message.surgeUsed == FIRST) {
								Surges.transformation(player.getEntityWorld(), pos, shiftHeld);
							} else if (message.surgeUsed == SECOND) {
								Surges.illumination(player, pos, shiftHeld);
							}
							
							break;
						case StormlightCapability.ELSECALLERS:
							if (message.surgeUsed == FIRST) {
								Surges.transportation(player, shiftHeld);
							} else if (message.surgeUsed == SECOND) {
								Surges.transformation(player.getEntityWorld(), pos, shiftHeld);
							}
							
							break;
						case StormlightCapability.WILLSHAPERS:
							if (message.surgeUsed == FIRST) {
								Surges.cohesion(player.getEntityWorld(),pos,shiftHeld);
							} else if (message.surgeUsed == SECOND) {
								Surges.transportation(player, shiftHeld);
							}
							
							break;
						case StormlightCapability.STONEWARDS:
							if (message.surgeUsed == FIRST) {
								Surges.tension(player.getEntityWorld(),pos,shiftHeld);
							} else if (message.surgeUsed == SECOND) {
								Surges.cohesion(player.getEntityWorld(),pos,shiftHeld);
							}
							
							break;
						case StormlightCapability.BONDSMITHS:
							if (message.surgeUsed == FIRST) {
								Surges.adhesion(player.getEntityWorld(), pos, shiftHeld);
							} else if (message.surgeUsed == SECOND) {
								Surges.tension(player.getEntityWorld(),pos,shiftHeld);
							}
							
							break;
						default:
							break;
						}
					}

				}
			});
			return null;
		}
	}
}
