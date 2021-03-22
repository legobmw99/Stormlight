package com.legobmw99.stormlight.modules.combat.item;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ShardbladeItem extends SwordItem {

    private static final int ATTACK_DAMAGE = 12;
    private static final float ATTACK_SPEED = 10.0f;

    private static final IItemTier SHARD = new IItemTier() {
        @Override
        public int getUses() {
            return -1;
        }

        @Override
        public float getSpeed() {
            return ATTACK_SPEED;
        }

        @Override
        public float getAttackDamageBonus() {
            return 0;
        }

        @Override
        public int getLevel() {
            return 6;
        }

        @Override
        public int getEnchantmentValue() {
            return 14;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(WorldSetup.INFUSED_SPHERES.stream().map(r -> new ItemStack(r.get())));
        }
    };


    private final Order order;

    public ShardbladeItem(Order order) {
        super(SHARD, ATTACK_DAMAGE, ATTACK_SPEED, Stormlight.createStandardItemProperties().stacksTo(1).fireResistant());
        this.order = order;

    }

    public Order getOrder() {
        return order;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        IFormattableTextComponent lore = new TranslationTextComponent("item.stormlight.shardblade.lore"); // todo
        lore.setStyle(lore.getStyle().withColor(Color.fromLegacyFormat(TextFormatting.AQUA)));
        tooltip.add(lore);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.RARE;
    }

}
