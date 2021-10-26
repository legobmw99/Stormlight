package com.legobmw99.stormlight.modules.powers.container;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;

public class PortableCraftingContainer extends CraftingMenu {
    public PortableCraftingContainer(int p_i50089_1_, Inventory p_i50089_2_) {
        super(p_i50089_1_, p_i50089_2_);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.hasEffect(PowersSetup.STORMLIGHT.get());
    }

}
