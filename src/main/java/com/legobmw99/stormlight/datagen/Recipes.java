package com.legobmw99.stormlight.datagen;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    private final Map<Character, Ingredient> defaultIngredients = new HashMap<>();

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
        add('g', Tags.Items.GLASS);
        add('d', Items.DIAMOND);
        add('e', Items.EMERALD);
        add('q', Items.QUARTZ);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        buildShaped(consumer, WorldSetup.DUN_SPHERES.get(0).get(), 4, Items.DIAMOND, "ggg", "gdg", "ggg");
        buildShaped(consumer, WorldSetup.DUN_SPHERES.get(1).get(), 4, Items.EMERALD, "ggg", "geg", "ggg");
        buildShaped(consumer, WorldSetup.DUN_SPHERES.get(2).get(), 4, Items.QUARTZ, "ggg", "gqg", "ggg");
    }


    protected void buildShaped(Consumer<FinishedRecipe> consumer, ItemLike result, int count, Item criterion, String... lines) {
        Stormlight.LOGGER.debug("Creating Shaped Recipe for " + result.asItem().getRegistryName());

        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(result, count);

        builder.unlockedBy("has_" + criterion.getRegistryName().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(criterion));

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

    protected void buildShaped(Consumer<FinishedRecipe> consumer, ItemLike result, Item criterion, String... lines) {
        buildShaped(consumer, result, 1, criterion, lines);
    }

    protected void add(char c, Tag.Named<Item> itemTag) {
        this.defaultIngredients.put(c, Ingredient.of(itemTag));
    }

    protected void add(char c, ItemLike itemProvider) {
        this.defaultIngredients.put(c, Ingredient.of(itemProvider));
    }

    protected void add(char c, Ingredient ingredient) {
        this.defaultIngredients.put(c, ingredient);
    }

    @Override
    public String getName() {
        return "Stormlight recipes";
    }
}
