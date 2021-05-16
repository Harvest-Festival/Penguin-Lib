package uk.joshiejack.penguinlib.item.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

@SuppressWarnings("NullableProblems")
public class AbstractSimplePenguinRecipe<T> implements IRecipe<IInventory> {
    protected final ResourceLocation rl;
    protected final Ingredient ingredient;
    protected final T output;
    protected final IRecipeType<?> type;
    protected final IRecipeSerializer<?> serializer;

    public AbstractSimplePenguinRecipe(IRecipeType<?> recipeType, IRecipeSerializer<?> recipeSerializer, ResourceLocation rl, Ingredient ingredient, T output) {
        this.type = recipeType;
        this.serializer = recipeSerializer;
        this.rl = rl;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean matches(IInventory inventory, @Nonnull World world) {
        return ingredient.test(inventory.getItem(0));
    }

    @Override
    public ItemStack assemble(@Nonnull IInventory inventory) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return rl;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return serializer;
    }

    @Override
    public IRecipeType<?> getType() {
        return type;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }
}
