package com.legobmw99.stormlight.modules.world.item;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.powers.StormlightEffect;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.util.Gemstone;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SphereItem extends Item {

    private boolean infused;
    private Gemstone setting;

    public SphereItem(boolean infused, Gemstone settting) {
        super(Stormlight.createStandardItemProperties().stacksTo(16));
        this.infused = infused;
        this.setting = setting;

    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 5;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand hand) {
        if (infused) {
            playerIn.swing(hand);
            return new ActionResult(ActionResultType.SUCCESS, playerIn.getItemInHand(hand));
        } else {
            return new ActionResult(ActionResultType.FAIL, playerIn.getItemInHand(hand));
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        // Don't consume items in creative mode
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (!player.abilities.instabuild) {
                stack.shrink(1);
                player.inventory.add(new ItemStack(this.swap(), 1, stack.getTag()));
            }

            if (!worldIn.isClientSide) {
                StormlightEffect.addOrUpdateEffect(player, this.setting.getModifier());
            }
        }
        return stack;
    }

    public SphereItem swap() {
        if (infused) {
            return WorldSetup.DUN_SPHERES.get(setting.getIndex()).get();
        } else {
            return WorldSetup.INFUSED_SPHERES.get(setting.getIndex()).get();
        }
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return infused ? Rarity.RARE : Rarity.COMMON;
    }


}
