package com.legobmw99.stormlight.modules.powers;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.util.Ideal;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

    private final LazyOptional<StormlightCapability> handler;

    private Order order ;
    private Ideal ideal;
    private ItemStack blade;
    private UUID sprenID;

    public StormlightCapability() {
        this.handler = LazyOptional.of(() -> this);
        this.order = null;
        this.ideal = Ideal.UNINVESTED;
        this.blade = ItemStack.EMPTY;
        this.sprenID = null;
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

    public boolean isKnight() { return order != null && ideal != Ideal.UNINVESTED && ideal != Ideal.TRAITOR; }

    public Ideal getIdeal() {
        return ideal;
    }

    public Ideal progressIdeal() {
        this.ideal = this.ideal.progressIdeal();
        return this.ideal;
    }

    public Ideal regressIdeal() {
        this.ideal = this.ideal.regressIdeal();
        return this.ideal;
    }

    public void storeBlade(ItemStack blade) {
        this.blade = blade.copy();
    }

    public boolean isBladeStored() {
        return !blade.isEmpty();
    }

    public boolean addBladeToInventory(PlayerEntity player) {
        if (player.inventory.add(this.blade)) {
            this.blade = ItemStack.EMPTY;
            return true;
        }
        return false;
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
        nbt.putInt("ideal", this.getIdeal().getIndex());
        nbt.put("blade", this.blade.serializeNBT());

        if (getSprenID() != null) {
            nbt.putUUID("sprenID", this.getSprenID());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.order = Order.getOrNull(nbt.getInt("order"));
        this.ideal = Ideal.get(nbt.getInt("ideal"));
        this.blade = ItemStack.of(nbt.getCompound("blade"));

        if (nbt.contains("sprenID")) {
            this.sprenID = nbt.getUUID("sprenID");
        }
    }

    @Override
    public String toString() {
        return "Order: " + order + ", Ideal: " + ideal + ", Blade: " + blade + ", Spren: " + sprenID;
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
