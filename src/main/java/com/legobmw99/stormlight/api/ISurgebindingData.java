package com.legobmw99.stormlight.api;

import com.legobmw99.stormlight.util.Ideal;
import com.legobmw99.stormlight.util.Order;
import com.legobmw99.stormlight.util.Surge;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface ISurgebindingData {

    @Nullable
    Order getOrder();

    void setOrder(Order order);

    Ideal getIdeal();

    void setIdeal(Ideal ideal);

    void storeBlade(@Nonnull ItemStack blade);


    boolean isBladeStored();

    boolean addBladeToInventory(Player player);

    UUID getSprenID();

    void setSprenID(UUID sprenID);


    default boolean isKnight() {
        return getOrder() != null && getIdeal() != Ideal.UNINVESTED && getIdeal() != Ideal.TRAITOR;
    }

    default Ideal progressIdeal() {
        Ideal next = getIdeal().progressIdeal();
        setIdeal(next);
        return next;
    }

    default Ideal regressIdeal() {
        Ideal next = getIdeal().regressIdeal();
        setIdeal(next);
        return next;
    }

    default boolean earnedBlade() {
        Order order = getOrder();
        return order != null && order.getBladeIdeal().compareTo(getIdeal()) < 0;
    }

    default boolean canUseSurge(Surge surge) {
        Order order = getOrder();
        Ideal ideal = getIdeal();
        return order != null &&
               ((surge == order.getFirst() && order.getFirstIdeal().compareTo(ideal) < 0) || (surge == order.getSecond() && order.getSecondIdeal().compareTo(ideal) < 0));
    }

    void load(CompoundTag nbt);

    CompoundTag save();
}
