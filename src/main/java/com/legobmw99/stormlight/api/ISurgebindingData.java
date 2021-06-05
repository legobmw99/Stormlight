package com.legobmw99.stormlight.api;

import com.legobmw99.stormlight.util.Ideal;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface ISurgebindingData {

    Order getOrder();

    void setOrder(Order order);

    boolean isKnight();

    Ideal getIdeal();

    void setIdeal(Ideal ideal);

    Ideal progressIdeal();

    Ideal regressIdeal();

    void storeBlade(@Nonnull ItemStack blade);

    ItemStack getBlade();

    boolean isBladeStored();

    boolean addBladeToInventory(PlayerEntity player);

    UUID getSprenID();

    void setSprenID(UUID sprenID);
}
