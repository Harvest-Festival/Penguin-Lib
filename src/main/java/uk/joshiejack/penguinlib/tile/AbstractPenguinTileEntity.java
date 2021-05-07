package uk.joshiejack.penguinlib.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public abstract class AbstractPenguinTileEntity extends TileEntity {
    public AbstractPenguinTileEntity(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()  {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        load(getBlockState(), packet.getTag());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    protected void markUpdated() {
        this.setChanged();
        assert this.level != null;
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }
}