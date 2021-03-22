package com.legobmw99.stormlight.modules.powers;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class StormlightCapability implements ICapabilitySerializable<CompoundNBT> {


    @CapabilityInject(StormlightCapability.class)
    public static final Capability<StormlightCapability> PLAYER_CAP = null;

    public static final ResourceLocation IDENTIFIER = new ResourceLocation(Stormlight.MODID, "surgebinding_data");


    public static final int WINDRUNNERS = 0, SKYBREAKERS = 1, DUSTBRINGERS = 2, EDGEDANCERS = 3, TRUTHWATCHERS = 4, LIGHTWEAVERS = 5, ELSECALLERS = 6, WILLSHAPERS = 7, STONEWARDS = 8, BONDSMITHS = 9;


    private final LazyOptional<StormlightCapability> handler;

    private Order order = null;
    private int ideal = 0;
    private boolean bladeStored = true;
    private UUID sprenID;

    public StormlightCapability() {
        this.handler = LazyOptional.of(() -> this);
    }

    public static StormlightCapability forPlayer(Entity player) {
        //noinspection ConstantConditions
        return player.getCapability(PLAYER_CAP).orElseThrow(() -> new RuntimeException("Capability not attached!"));
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(StormlightCapability.class, new StormlightCapability.Storage(), () -> null);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getIdeal() {
        return ideal;
    }

    public int progressIdeal() {
        return ++this.ideal;
    }

    public int decrementIdeal() {
        if (this.ideal > 0) {
            return --this.ideal;
        }
        return 0;
    }

    public boolean isBladeStored() {
        return bladeStored;
    }

    public void setBladeStored(boolean bladeStored) {
        this.bladeStored = bladeStored;
    }

    public UUID getSprenID() {
        return sprenID;
    }

    public void setSprenID(UUID sprenID) {
        this.sprenID = sprenID;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putInt("order", this.getOrder() != null ? this.getOrder().getIndex() : -1);
        nbt.putInt("ideal", this.getIdeal());
        nbt.putBoolean("bladeStored", this.isBladeStored());
        if (getSprenID() != null) {
            nbt.putUUID("sprenID", this.getSprenID());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.order = Order.getOrNull(nbt.getInt("order"));
        this.ideal = nbt.getInt("ideal");
        this.bladeStored = nbt.getBoolean("bladeStored");
        if (nbt.contains("sprenID")) {
            this.sprenID = nbt.getUUID("sprenID");
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return PLAYER_CAP.orEmpty(cap, this.handler);
    }


    public static class Storage implements Capability.IStorage<StormlightCapability> {

        @Override
        public INBT writeNBT(Capability<StormlightCapability> capability, StormlightCapability instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<StormlightCapability> capability, StormlightCapability instance, Direction side, INBT nbt) {
            if (nbt instanceof CompoundNBT) {
                instance.deserializeNBT((CompoundNBT) nbt);
            }
        }
    }


}
