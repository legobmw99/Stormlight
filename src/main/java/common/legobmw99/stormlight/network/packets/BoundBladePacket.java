
package common.legobmw99.stormlight.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
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
	public BoundBladePacket(){}

	
	
	private int entityID;
	private String type;
	private int sendorrecieve;
	public BoundBladePacket(int entityID, String type, int sor) {

		this.entityID = entityID;
		this.type = type;
		this.sendorrecieve = sor;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		

		entityID =  ByteBufUtils.readVarInt(buf, 5);
		type = ByteBufUtils.readUTF8String(buf);
		sendorrecieve =ByteBufUtils.readVarInt(buf, 5);

	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		ByteBufUtils.writeVarInt(buf, entityID, 5);		
		ByteBufUtils.writeUTF8String(buf, type);
		ByteBufUtils.writeVarInt(buf,(int)(sendorrecieve), 5);

	}

	public static class Handler implements IMessageHandler<BoundBladePacket, IMessage>{

		@Override
		public IMessage onMessage(final BoundBladePacket message, final MessageContext ctx) {
	        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world; // or Minecraft.getMinecraft() on the client
	        mainThread.addScheduledTask(new Runnable() {
	            @Override
	            public void run() {
	        		EntityPlayerMP player = (EntityPlayerMP) ctx.getServerHandler().playerEntity.world.getEntityByID(message.entityID);
	        		if (player == null) {
	        			return;
	        		} else {
    					InventoryEnderChest inv = player.getInventoryEnderChest();

	        			switch (message.sendorrecieve){
	        			case 0:
	        				//Send to EChest
	        				if(player.getHeldItemMainhand().isItemEqual(new ItemStack(Item.getByNameOrId("stormlight:honorblade." + message.type)))){
	        					if(inv != null){
	        						for(int i=0; i < inv.getSizeInventory(); i++){
	        							if (inv.getStackInSlot(i) == (ItemStack)null){ 
	        								inv.setInventorySlotContents(i,player.getHeldItemMainhand());
	        								player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack) null);
	        								break;
	        							} else {
	        								continue;
	        							}
	        						}
	        					}
	        				}
	        			
	        				break;
	        			case 1:
	        				//Get from EChest
	        				if(player.getHeldItemMainhand() == (ItemStack)null){
	        					if(inv != null){
	        						for(int i=0; i < inv.getSizeInventory(); i++){
	        							if (inv.getStackInSlot(i) != (ItemStack)null){ 
	        								if(inv.getStackInSlot(i).getUnlocalizedName().contains("honorblade.")){
	        									player.inventory.setInventorySlotContents(player.inventory.currentItem,inv.getStackInSlot(i));
	        									inv.removeStackFromSlot(i);
	        									break;
	        								} else{
	        									continue;
	        								}
	        							}
	        						}
	        					}
	        				}
	        				break;
	        			}

	        		}	            
	        		}
	        });		return null;
		}
	}
}