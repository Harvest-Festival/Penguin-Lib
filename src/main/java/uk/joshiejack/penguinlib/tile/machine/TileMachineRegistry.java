package uk.joshiejack.penguinlib.tile.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public abstract class TileMachineRegistry extends AbstractSimpleMachineTileEntity {
    protected final IRecipeType<? extends SingleItemRecipe> recipeType;

    public TileMachineRegistry(TileEntityType<?> type, IRecipeType<? extends SingleItemRecipe> recipeType, String time) {
        super(type, time);
        this.recipeType = recipeType;
    }

    @SuppressWarnings("ConstantConditions")
    protected ItemStack getResult(ItemStack stack) {
        for (IRecipe<?> recipe: level.getRecipeManager().getAllRecipesFor(recipeType)) {
            if (recipe.getIngredients().stream().allMatch(ing -> ing.test(stack)))
                return recipe.getResultItem();
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
        return items.get(slot).isEmpty() && !getResult(stack).isEmpty();
    }

    @Override
    public void finishMachine() {
        items.set(0, getResult(items.get(0)).copy()); //Hell yeah!
        setChanged();
    }
}

