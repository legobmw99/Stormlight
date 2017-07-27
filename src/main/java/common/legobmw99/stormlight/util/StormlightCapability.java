package common.legobmw99.stormlight.util;

import java.util.concurrent.Callable;

import common.legobmw99.stormlight.Stormlight;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class StormlightCapability implements ICapabilitySerializable<NBTTagCompound>{
	
    public static final ResourceLocation IDENTIFIER = new ResourceLocation(Stormlight.MODID, "surgebinding_data");
    public static final int WINDRUNNERS = 0, SKYBREAKERS = 1, DUSTBRINGERS = 2, EDGEDANCERS = 3, TRUTHWATCHERS = 4, LIGHTWEAVERS = 5, ELSECALLERS = 6, WILLSHAPERS = 7, STONEWARDS = 8, BONDSMITHS = 9;
    
    private int surgebindingType = -1;
	private int progression = -1;
    private boolean bladeStored = true;
    
    public static StormlightCapability forPlayer(Entity e) {
        return e.getCapability(Stormlight.PLAYER_CAP, null);
        
    }
    
    public int getType() {
		return surgebindingType;
	}

	public void setType(int type) {
		this.surgebindingType = type;
	}

	public int getProgression() {
		return progression;
	}

	public void setProgression(int p) {
		this.progression = p;
	}

	public boolean isBladeStored() {
		return bladeStored;
	}

	public void setBladeStored(boolean bladeStored) {
		this.bladeStored = bladeStored;
	}
	
    
    public static void register() {
        CapabilityManager.INSTANCE.register(StormlightCapability.class, new StormlightCapability.Storage(), new StormlightCapability.Factory());
    }
    

	@Override
	public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("surgebindingType", this.getType());
        nbt.setInteger("progression", this.getProgression());
        nbt.setBoolean("bladeStored", this.isBladeStored());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.surgebindingType = nbt.getInteger("surgebindingType");
		this.progression = nbt.getInteger("progression");
		this.bladeStored = nbt.getBoolean("bladeStored");
	}
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return Stormlight.PLAYER_CAP != null && capability == Stormlight.PLAYER_CAP;

    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return Stormlight.PLAYER_CAP != null && capability == Stormlight.PLAYER_CAP ? (T) this : null;
    }

    public static class Storage implements Capability.IStorage<StormlightCapability> {

        @Override
        public NBTBase writeNBT(Capability<StormlightCapability> capability, StormlightCapability instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<StormlightCapability> capability, StormlightCapability instance, EnumFacing side, NBTBase nbt) {

        }

    }

    public static class Factory implements Callable<StormlightCapability> {
        @Override
        public StormlightCapability call() throws Exception {
            return null;
        }
    }


}
