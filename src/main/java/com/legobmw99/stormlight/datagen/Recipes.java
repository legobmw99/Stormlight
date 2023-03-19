package com.legobmw99.stormlight.datagen;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    private final Map<Character, Ingredient> defaultIngredients = new HashMap<>();

    public Recipes(PackOutput generatorIn) {
        super(generatorIn);
        add('g', Tags.Items.GLASS);
        add('d', Items.DIAMOND);
        add('e', Items.EMERALD);
        add('q', Items.QUARTZ);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

        buildShaped(consumer, RecipeCategory.MISC, WorldSetup.DUN_SPHERES.get(0).get(), 4, Items.DIAMOND, "ggg", "gdg", "ggg");
        buildShaped(consumer, RecipeCategory.MISC, WorldSetup.DUN_SPHERES.get(1).get(), 4, Items.EMERALD, "ggg", "geg", "ggg");
        buildShaped(consumer, RecipeCategory.MISC, WorldSetup.DUN_SPHERES.get(2).get(), 4, Items.QUARTZ, "ggg", "gqg", "ggg");
    }

    protected void buildShaped(Consumer<FinishedRecipe> consumer, RecipeCategory cat, ItemLike result, int count, Item criterion, String... lines) {
        Stormlight.LOGGER.debug("Creating Shaped Recipe for " + ForgeRegistries.ITEMS.getKey(result.asItem()));

        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(cat, result, count);

        builder.unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(criterion).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(criterion));

        Set<Character> characters = new HashSet<>();
        for (String line : lines) {
            builder.pattern(line);
            line.chars().forEach(value -> characters.add((char) value));
        }

        for (Character c : characters) {
            if (this.defaultIngredients.containsKey(c)) {
                builder.define(c, this.defaultIngredients.get(c));
            }
        }

        builder.save(consumer);
    }


    protected void add(char c, TagKey<Item> itemTag) {
        this.defaultIngredients.put(c, Ingredient.of(itemTag));
    }

    protected void add(char c, ItemLike itemProvider) {
        this.defaultIngredients.put(c, Ingredient.of(itemProvider));
    }

    protected void add(char c, Ingredient ingredient) {
        this.defaultIngredients.put(c, ingredient);
    }


}
