package uk.joshiejack.penguinlib.tile.machine;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import uk.joshiejack.penguinlib.block.base.AbstractDoubleBlock;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.SetInventorySlotPacket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public abstract class AbstractIRecipeMachine extends AbstractSimpleMachineTileEntity {
    protected final IRecipeType<? extends SingleItemRecipe> recipeType;

    public AbstractIRecipeMachine(TileEntityType<?> type, IRecipeType<? extends SingleItemRecipe> recipeType, String time) {
        super(type, time);
        this.recipeType = recipeType;
    }

    @Override
    public int getMaxStackSize() {
        return items.get(0).isEmpty() ? 1 : items.get(0).getMaxStackSize();
    }

    @SuppressWarnings("ConstantConditions")
    protected ItemStack getResult(ItemStack stack) {
        for (IRecipe<?> recipe : level.getRecipeManager().getAllRecipesFor(recipeType)) {
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
    protected boolean canStart() {
        return !getResult(items.get(0)).isEmpty();
    }

    @Override
    public void finishMachine() {
        ItemStack result = getResult(items.get(0)).copy();
        items.set(0, result); //Hell yeah!
        PenguinNetwork.sendToNearby(new SetInventorySlotPacket(worldPosition, 0, result), this);
        setChanged();
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        BlockState state = getBlockState();
        if (state.getBlock() instanceof AbstractDoubleBlock
                && state.getValue(AbstractDoubleBlock.HALF) == DoubleBlockHalf.UPPER) {
            TileEntity below = Objects.requireNonNull(level).getBlockEntity(worldPosition.below());
            BlockState belowState = Objects.requireNonNull(level).getBlockState(worldPosition.below());
            return below != null && belowState.getBlock() == state.getBlock() ? below.getCapability(cap, side) : LazyOptional.empty();
        }

        return super.getCapability(cap, side);
    }
}