package com.legobmw99.stormlight.modules.powers.data;

import com.legobmw99.stormlight.api.ISurgebindingData;
import com.legobmw99.stormlight.util.Ideal;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class DefaultSurgebindingData implements ISurgebindingData {

    private Order order;
    private Ideal ideal;
    private ItemStack blade;
    private UUID sprenID;

    public DefaultSurgebindingData() {
        this.order = null;
        this.ideal = Ideal.UNINVESTED;
        this.blade = ItemStack.EMPTY;
        this.sprenID = null;
    }

    @Nullable
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Ideal getIdeal() {
        return ideal;
    }

    public void setIdeal(Ideal ideal) {
        this.ideal = ideal;
    }

    public void storeBlade(@Nonnull ItemStack blade) {
        this.blade = blade.copy();
    }

    public ItemStack getBlade() {return this.blade.copy(); }

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
    public String toString() {
        return "Order: " + order + ", Ideal: " + ideal + ", Blade: " + blade + ", Spren: " + sprenID;
    }


}
