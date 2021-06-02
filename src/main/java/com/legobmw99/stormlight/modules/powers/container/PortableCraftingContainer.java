package com.legobmw99.stormlight.modules.powers.container;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.WorkbenchContainer;

public class PortableCraftingContainer extends WorkbenchContainer {
    public PortableCraftingContainer(int p_i50089_1_, PlayerInventory p_i50089_2_) {
        super(p_i50089_1_, p_i50089_2_);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return player.hasEffect(PowersSetup.STORMLIGHT.get());
    }

}
