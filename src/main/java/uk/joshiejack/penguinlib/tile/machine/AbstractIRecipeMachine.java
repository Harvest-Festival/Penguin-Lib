package uk.joshiejack.penguinlib.tile.machine;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
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

public abstract class AbstractIRecipeMachine<I extends IRecipe<IInventory>> extends AbstractSimpleMachineTileEntity {
    protected final IRecipeType<I> recipeType;

    public AbstractIRecipeMachine(TileEntityType<?> type, IRecipeType<I> recipeType) {
        this(type, recipeType, Objects.requireNonNull(type.getRegistryName()).toString());
    }

    public AbstractIRecipeMachine(TileEntityType<?> type, IRecipeType<I> recipeType, String time) {
        super(type, time);
        this.recipeType = recipeType;
    }

    @Override
    public int getMaxStackSize() {
        return items.get(0).isEmpty() ? 1 : items.get(0).getMaxStackSize();
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    protected I getRecipeResult(ItemStack stack) {
        for (I recipe : level.getRecipeManager().getAllRecipesFor(recipeType)) {
            if (recipe.getIngredients().stream().allMatch(ing -> ing.test(stack)))
                return recipe;
        }

        return null;
    }

    @Nonnull
    protected ItemStack getResult(ItemStack stack) {
        IRecipe<?> recipe = getRecipeResult(stack);
        return recipe == null ? ItemStack.EMPTY : recipe.getResultItem();
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
        ItemStack result = Objects.requireNonNull(getRecipeResult(items.get(0))).assemble(this);
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