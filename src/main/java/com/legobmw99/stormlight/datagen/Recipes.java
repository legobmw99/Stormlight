package com.legobmw99.stormlight.datagen;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
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
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        buildShaped(consumer, WorldSetup.DUN_SPHERES.get(0).get(), 4, Items.DIAMOND, "ggg", "gdg", "ggg");
        buildShaped(consumer, WorldSetup.DUN_SPHERES.get(1).get(), 4, Items.EMERALD, "ggg", "geg", "ggg");
        buildShaped(consumer, WorldSetup.DUN_SPHERES.get(2).get(), 4, Items.QUARTZ, "ggg", "gqg", "ggg");
    }


    protected void buildShaped(Consumer<IFinishedRecipe> consumer, IItemProvider result, int count, Item criterion, String... lines) {
        Stormlight.LOGGER.debug("Creating Shaped Recipe for " + result.asItem().getRegistryName());

        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(result, count);

        builder.unlockedBy("has_" + criterion.getRegistryName().getPath(), InventoryChangeTrigger.Instance.hasItems(criterion));

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

    protected void buildShaped(Consumer<IFinishedRecipe> consumer, IItemProvider result, Item criterion, String... lines) {
        buildShaped(consumer, result, 1, criterion, lines);
    }

    protected void add(char c, ITag.INamedTag<Item> itemTag) {
        this.defaultIngredients.put(c, Ingredient.of(itemTag));
    }

    protected void add(char c, IItemProvider itemProvider) {
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
