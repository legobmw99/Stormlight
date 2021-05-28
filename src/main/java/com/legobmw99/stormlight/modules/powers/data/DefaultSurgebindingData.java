package com.legobmw99.stormlight.modules.powers.data;

import com.legobmw99.stormlight.api.ISurgebindingData;
import com.legobmw99.stormlight.util.Ideal;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

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

    public void setIdeal(Ideal ideal) {
        this.ideal = ideal;
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
