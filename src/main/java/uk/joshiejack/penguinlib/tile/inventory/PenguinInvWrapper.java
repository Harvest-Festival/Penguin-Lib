package uk.joshiejack.penguinlib.tile.inventory;

import net.minecraftforge.items.wrapper.InvWrapper;

public class PenguinInvWrapper extends InvWrapper {
    private final AbstractInventoryTileEntity tileEntity;

    public PenguinInvWrapper(AbstractInventoryTileEntity inv) {
        super(inv);
        this.tileEntity = inv;
    }

    @Override
    public int getSlotLimit(int slot) {
        return tileEntity.getSlotLimit(slot);
    }
}
