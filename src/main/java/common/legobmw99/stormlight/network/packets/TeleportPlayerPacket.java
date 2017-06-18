package common.legobmw99.stormlight.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class TeleportPlayerPacket implements IMessage {
	 
public TeleportPlayerPacket(){}

	
	

	private int entityID;
	private int shiftHeld;

	public TeleportPlayerPacket(int entityID, int shiftHeld) {

		this.entityID = entityID;
		this.shiftHeld = shiftHeld;


	}
	@Override
	public void fromBytes(ByteBuf buf) {
		
		entityID =  ByteBufUtils.readVarInt(buf, 5);
		shiftHeld =  ByteBufUtils.readVarInt(buf, 5);


	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		ByteBufUtils.writeVarInt(buf, entityID, 5);	
		ByteBufUtils.writeVarInt(buf, shiftHeld, 5);		

	}

	public static class Handler implements IMessageHandler<TeleportPlayerPacket, IMessage>{

		@Override
		public IMessage onMessage(final TeleportPlayerPacket message, final MessageContext ctx) {
	        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world; // or Minecraft.getMinecraft() on the client
	        mainThread.addScheduledTask(new Runnable() {
	            @Override
	            public void run() {
	        		EntityPlayerMP player = (EntityPlayerMP) ctx.getServerHandler().player.world.getEntityByID(message.entityID);
	        		if (player == null) {
	        			return;
	        		} else {
	        			if(message.shiftHeld == 0){
	        			EntityEnderPearl entityenderpearl = new EntityEnderPearl(ctx.getServerHandler().player.world, player);
	                    entityenderpearl.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.0F, 0.0F);
	                    ctx.getServerHandler().player.world.spawnEntity(entityenderpearl);
	        			} else {
	        				if(player.dimension != 0){
	        					player.changeDimension(0);
	        				}
	        				if(player.getBedLocation() != null && player.getBedSpawnLocation(player.world, player.getBedLocation(), false) != null){
	        					player.connection.setPlayerLocation(player.getBedSpawnLocation(player.world, player.getBedLocation(), false).getX(),player.getBedSpawnLocation(player.world, player.getBedLocation(), false).getY(),player.getBedSpawnLocation(player.world, player.getBedLocation(), false).getZ(), player.cameraYaw, player.cameraPitch);
	        				} else {
	        					player.connection.setPlayerLocation(ctx.getServerHandler().player.world.getSpawnPoint().getX(),ctx.getServerHandler().player.world.getSpawnPoint().getY(),ctx.getServerHandler().player.world.getSpawnPoint().getZ(), player.cameraYaw, player.cameraPitch);
	        				}
	        			}
	        		}            
	        	}
	        });		return null;
		}
	}
}