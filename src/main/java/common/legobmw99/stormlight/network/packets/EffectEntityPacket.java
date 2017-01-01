package common.legobmw99.stormlight.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EffectEntityPacket implements IMessage {
	public EffectEntityPacket(){}

	
	
	private int id;
	private int duration;
	private int entityID;
	private int level;
	public EffectEntityPacket(int id, int duration, int level, int entityID) {

		this.entityID = entityID;
		this.id = id;
		this.duration = duration;
		this.level = level;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		
		id = ByteBufUtils.readVarInt(buf, 5);
		duration =  ByteBufUtils.readVarInt(buf, 5);
		level = ByteBufUtils.readVarInt(buf, 5);
		entityID =  ByteBufUtils.readVarInt(buf, 5);

	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		ByteBufUtils.writeVarInt(buf,(int)(id), 5);
		ByteBufUtils.writeVarInt(buf,(int)(duration), 5);
		ByteBufUtils.writeVarInt(buf,(int)(level), 5);
		ByteBufUtils.writeVarInt(buf, entityID, 5);		
	}

	public static class Handler implements IMessageHandler<EffectEntityPacket, IMessage>{

		@Override
		public IMessage onMessage(final EffectEntityPacket message, final MessageContext ctx) {
	        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world; // or Minecraft.getMinecraft() on the client
	        mainThread.addScheduledTask(new Runnable() {
	            @Override
	            public void run() {
	        		EntityPlayerMP target = (EntityPlayerMP) ctx.getServerHandler().playerEntity.world.getEntityByID(message.entityID);
	        		if (target == null) {
	        			return;
	        		} else {
	        			target.removePotionEffect(Potion.getPotionById(message.id));
	        			if(message.duration > 1){
	        				target.addPotionEffect(new PotionEffect(Potion.getPotionById(message.id), message.duration, message.level, true, false));
	        			}

	        		}	            
	        		}
	        });		return null;
		}
	}
}