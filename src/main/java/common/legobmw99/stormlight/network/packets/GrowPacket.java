package common.legobmw99.stormlight.network.packets;


import io.netty.buffer.ByteBuf;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GrowPacket implements IMessage {
	public GrowPacket(){}

	
	
	private double X;
	private double Y;
	private double Z;

	public GrowPacket(double X, double Y,double Z) {
		this.X = X;
		this.Y = Y;
		this.Z = Z;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		
		//Because floats aren't applicable, divide to get decimals back
		X = ((double) ByteBufUtils.readVarInt(buf, 5));
		Y = ((double) ByteBufUtils.readVarInt(buf, 5));
		Z = ((double) ByteBufUtils.readVarInt(buf, 5));

	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		//Because floats aren't applicable, multiply to get some decimals
		ByteBufUtils.writeVarInt(buf,(int)(X), 5);
		ByteBufUtils.writeVarInt(buf,(int)(Y), 5);
		ByteBufUtils.writeVarInt(buf, (int)(Z), 5);
	}

	public static class Handler implements IMessageHandler<GrowPacket, IMessage>{

		@Override
		public IMessage onMessage(final GrowPacket message, final MessageContext ctx) {
	        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world; // or Minecraft.getMinecraft() on the client
	        mainThread.addScheduledTask(new Runnable() {
	            @Override
	            public void run() {
	            	BlockPos bp = new BlockPos(message.X, message.Y, message.Z);
	            	IBlockState ibs = ctx.getServerHandler().playerEntity.world.getBlockState(bp);
		            IGrowable igrowable = (IGrowable)ibs.getBlock();

	            	 if (igrowable.canUseBonemeal(ctx.getServerHandler().playerEntity.world, ctx.getServerHandler().playerEntity.world.rand, bp, ibs))
	                    {
	                        igrowable.grow(ctx.getServerHandler().playerEntity.world, ctx.getServerHandler().playerEntity.world.rand, bp, ibs);
	                    }     
	        		}
	        });		return null;
		}
	}
}