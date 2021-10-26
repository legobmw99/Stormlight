package com.legobmw99.stormlight.modules.powers.data;

import com.legobmw99.stormlight.api.ISurgebindingData;
import com.legobmw99.stormlight.util.Ideal;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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

    public boolean isBladeStored() {
        return !blade.isEmpty();
    }

    public boolean addBladeToInventory(Player player) {
        if (player.getInventory().add(this.blade)) {
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

    @Override
    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt("order", this.getOrder() != null ? this.getOrder().getIndex() : -1);
        nbt.putInt("ideal", this.getIdeal().getIndex());
        nbt.put("blade", this.blade.copy().serializeNBT());

        if (this.getSprenID() != null) {
            nbt.putUUID("sprenID", this.getSprenID());
        }
        return nbt;
    }

    @Override
    public void load(CompoundTag surgebinding_data) {
        this.setOrder(Order.getOrNull(surgebinding_data.getInt("order")));
        this.ideal = (Ideal.get(surgebinding_data.getInt("ideal")));
        this.storeBlade(ItemStack.of(surgebinding_data.getCompound("blade")));

        if (surgebinding_data.contains("sprenID")) {
            this.setSprenID(surgebinding_data.getUUID("sprenID"));
        }
    }

}
