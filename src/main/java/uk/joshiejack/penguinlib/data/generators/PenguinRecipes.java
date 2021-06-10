package uk.joshiejack.penguinlib.data.generators;

import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.generators.builders.CategoryBuilder;
import uk.joshiejack.penguinlib.item.PenguinItems;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class PenguinRecipes extends RecipeProvider {
    public PenguinRecipes(DataGenerator generator) {
        super(generator);
    }

    private ResourceLocation rl(String name) {
        return new ResourceLocation(PenguinLib.MODID, name);
    }

    @Override
    protected void buildShapelessRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(PenguinItems.UNFIRED_PLATE.get()).define('C', Items.CLAY).pattern("CC").unlockedBy("has_furnace", has(Blocks.FURNACE)).save(consumer, rl("unfired_plate"));
        ShapedRecipeBuilder.shaped(PenguinItems.UNFIRED_MUG.get()).define('C', Items.CLAY).pattern("C").pattern("C").unlockedBy("has_furnace", has(Blocks.FURNACE)).save(consumer, rl("unfired_mug"));
        CookingRecipeBuilder.smelting(Ingredient.of(PenguinItems.UNFIRED_PLATE.get()), PenguinItems.PLATE.get(), 0.3F, 200).unlockedBy("has_furnace", has(Blocks.FURNACE)).save(consumer, rl("plate"));
        CookingRecipeBuilder.smelting(Ingredient.of(PenguinItems.UNFIRED_MUG.get()), PenguinItems.MUG.get(), 0.3F, 200).unlockedBy("has_furnace", has(Blocks.FURNACE)).save(consumer, rl("mug"));
        ShapedRecipeBuilder.shaped(PenguinItems.DEEP_BOWL.get(), 6).define('P', ItemTags.PLANKS).define('S', ItemTags.WOODEN_SLABS).pattern("P P").pattern("SSS").unlockedBy("has_planks", has(ItemTags.PLANKS)).save(consumer, rl("deep_bowl"));
        ShapedRecipeBuilder.shaped(PenguinItems.GLASS.get(), 4).define('G', Tags.Items.GLASS_COLORLESS).pattern("G G").pattern(" G ").unlockedBy("has_glass", has(Blocks.GLASS)).save(consumer, rl("glass"));
        ShapedRecipeBuilder.shaped(PenguinItems.JAM_JAR.get(), 6).define('G', Tags.Items.GLASS_COLORLESS).define('S', ItemTags.WOODEN_SLABS).pattern("S").pattern("G").unlockedBy("has_glass", has(Blocks.GLASS)).save(consumer, rl("jam_jar"));
        ShapedRecipeBuilder.shaped(PenguinItems.PICKLING_JAR.get(), 4).define('G', Tags.Items.GLASS_COLORLESS).define('S', Tags.Items.NUGGETS_IRON).pattern("S").pattern("G").unlockedBy("has_glass", has(Blocks.GLASS)).save(consumer, rl("pickling_jar"));
        //Note tests
        CategoryBuilder.category().withItemIcon(Items.EGG)
                .withNote("cats_page").withItemIcon(Items.COD).end()
                .withNote("tigers").withItemIcon(Items.JUNGLE_BUTTON).end()
                .withNote("lions").withItemIcon(Items.ACACIA_BOAT).setDefault().end()
                .withNote("jaguars").withItemIcon(Items.BAMBOO).end()
                .withNote("horse_radish").withPenguinIcon(0, 0).end()
                .withNote("banana_pickle").withPenguinIcon(0, 0).end()
                .save(consumer, rl("cats"));
    }
}
