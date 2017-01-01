package common.legobmw99.stormlight.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TransformBlockPacket implements IMessage{

public TransformBlockPacket(){}

	
	

	private int blockID;
	private int x,y,z;

	public TransformBlockPacket(int blockID, BlockPos pos) {

		this.blockID = blockID;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();


	}
	@Override
	public void fromBytes(ByteBuf buf) {
		
		blockID =  ByteBufUtils.readVarInt(buf, 5);
		x =  ByteBufUtils.readVarInt(buf, 5);
		y =  ByteBufUtils.readVarInt(buf, 5);
		z =  ByteBufUtils.readVarInt(buf, 5);

	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		ByteBufUtils.writeVarInt(buf, blockID, 5);	
		ByteBufUtils.writeVarInt(buf, x, 5);		
		ByteBufUtils.writeVarInt(buf, y, 5);	
		ByteBufUtils.writeVarInt(buf, z, 5);	
	}

	public static class Handler implements IMessageHandler<TransformBlockPacket, IMessage>{

		@Override
		public IMessage onMessage(final TransformBlockPacket message, final MessageContext ctx) {
	        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world; // or Minecraft.getMinecraft() on the client
	        mainThread.addScheduledTask(new Runnable() {
	            @Override
	            public void run() {
	            	ctx.getServerHandler().playerEntity.world.setBlockState(new BlockPos(message.x,message.y,message.z), (IBlockState) (Block.getBlockById(message.blockID).getBlockState().getBaseState()));
	        		}
	        });		return null;
		}
	}
}