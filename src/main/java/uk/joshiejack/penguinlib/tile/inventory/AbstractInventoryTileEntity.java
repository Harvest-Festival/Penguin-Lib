package uk.joshiejack.penguinlib.tile.inventory;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import uk.joshiejack.penguinlib.tile.AbstractPenguinTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("ConstantConditions")
public abstract class AbstractInventoryTileEntity extends AbstractPenguinTileEntity implements IInventory {
    protected final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(this::createHandler);
    protected final NonNullList<ItemStack> items;

    public AbstractInventoryTileEntity(TileEntityType<?> type, int size) {
        super(type);
        items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Nonnull
    protected IItemHandler createHandler() {
        return new PenguinInvWrapper(this);
    }

    public int getSlotLimit(int slot) {
        return getMaxStackSize();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemHandler.cast();
        return super.getCapability(cap, side);
    }

    @Nonnull
    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public void setItem(int slot, @Nonnull ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }

        markUpdated();
    }

    @Nonnull
    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ItemStackHelper.removeItem(items, slot, amount);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Nonnull
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ItemStackHelper.takeItem(items, slot);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean stillValid(@Nonnull PlayerEntity player) {
        if (level.getBlockEntity(worldPosition) != this)
            return false;
        else
            return player.distanceToSqr((double) worldPosition.getX() + 0.5D, (double) worldPosition.getY() + 0.5D, (double) worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    protected void loadInventory(@Nonnull CompoundNBT nbt) {
        ItemStackHelper.loadAllItems(nbt, items);
    }

    public CompoundNBT saveInventory(@Nonnull CompoundNBT nbt) {
        return ItemStackHelper.saveAllItems(nbt, items);
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        loadInventory(nbt.getCompound("Inventory"));
    }

    @Nonnull
    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.put("Inventory", saveInventory(new CompoundNBT()));
        return super.save(nbt);
    }
}
