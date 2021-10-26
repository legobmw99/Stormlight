package com.legobmw99.stormlight.modules.world.item;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.api.ISurgebindingData;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.modules.powers.effect.EffectHelper;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.util.Gemstone;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SphereItem extends Item {

    private final boolean infused;
    private final Gemstone setting;

    public SphereItem(boolean infused, Gemstone setting) {
        super(Stormlight.createStandardItemProperties().stacksTo(infused ? 1 : 4));
        this.infused = infused;
        this.setting = setting;

    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return this.infused ? UseAnim.DRINK : UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 5;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
        if (infused && player.getCapability(SurgebindingCapability.PLAYER_CAP).filter(ISurgebindingData::isKnight).isPresent()) {
            player.startUsingItem(hand);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, player.getItemInHand(hand));
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof Player player && this.infused) {
            // Don't consume items in creative mode
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                player.getInventory().add(new ItemStack(this.swap(), 1, stack.getTag()));
            }

            if (!worldIn.isClientSide) {
                EffectHelper.addOrUpdateEffect(player, this.setting.getModifier());
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
