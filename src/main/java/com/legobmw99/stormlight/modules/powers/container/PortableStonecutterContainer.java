package com.legobmw99.stormlight.modules.powers.container;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.StonecutterContainer;

public class PortableStonecutterContainer extends StonecutterContainer {

    public PortableStonecutterContainer(int p_i50060_1_, PlayerInventory p_i50060_2_) {
        super(p_i50060_1_, p_i50060_2_);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return player.hasEffect(PowersSetup.STORMLIGHT.get());
    }

}
