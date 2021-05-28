package com.legobmw99.stormlight.api;

import com.legobmw99.stormlight.util.Ideal;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public interface ISurgebindingData {

    Order getOrder();

    void setOrder(Order order);

    boolean isKnight();

    Ideal getIdeal();

    Ideal progressIdeal();

    Ideal regressIdeal();

    void setIdeal(Ideal ideal);

    void storeBlade(ItemStack blade);

    ItemStack getBlade();

    boolean isBladeStored();

    boolean addBladeToInventory(PlayerEntity player);

    UUID getSprenID();

    void setSprenID(UUID sprenID);
}
