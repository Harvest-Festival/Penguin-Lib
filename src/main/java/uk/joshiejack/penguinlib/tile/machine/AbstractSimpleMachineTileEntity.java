package uk.joshiejack.penguinlib.tile.machine;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import uk.joshiejack.penguinlib.data.database.registries.TimeUnitRegistry;

import javax.annotation.Nonnull;

public abstract class AbstractSimpleMachineTileEntity extends AbstractMachineTileEntity {
    private final String time;

    public AbstractSimpleMachineTileEntity(TileEntityType<?> type, String time) {
        super(type, 1);
        this.time = time;
    }

    @Override
    public long getOperationalTime() {
        return TimeUnitRegistry.get(time);
    }

    @Nonnull
    @Override
    public ItemStack removeItem(int slot, int amount) {
        return isActive() ? ItemStack.EMPTY : ItemStackHelper.removeItem(items, slot, amount);
    }

    @Override
    public void setItem(int slot, @Nonnull ItemStack stack){
        super.setItem(slot, stack);
        if (isActive() && stack.isEmpty()) {
            onMachineEmptied();
        } else if (canStart()) {
            startMachine();
        }
    }

    @Override
    protected boolean canStart() {
        return canPlaceItem(0, items.get(0));
    }

    protected void onMachineEmptied() {}
}

