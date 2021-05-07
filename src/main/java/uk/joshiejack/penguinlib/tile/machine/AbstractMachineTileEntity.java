package uk.joshiejack.penguinlib.tile.machine;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.SetActiveStatePacket;
import uk.joshiejack.penguinlib.tile.inventory.AbstractInventoryTileEntity;

import javax.annotation.Nonnull;

public abstract class AbstractMachineTileEntity extends AbstractInventoryTileEntity implements ITickableTileEntity {
    private boolean active;
    private long started;
    private long passed;

    public AbstractMachineTileEntity(TileEntityType<?> type, int size) {
        super(type, size);
    }

    //1000 = 60 minutes
    //500 = 30 minutes
    //250 = 15 minutes
    //50 = 3 minutes

    protected void startMachine() {
        active = true;
        assert level != null;
        started = level.getGameTime();
        if (!level.isClientSide) {
            PenguinNetwork.sendToNearby(new SetActiveStatePacket(worldPosition, true), this);
        }
    }
    public abstract void finishMachine();

    public abstract long getOperationalTime();

    @OnlyIn(Dist.CLIENT)
    public void setActive(boolean active) {
        this.active = active;
        this.markUpdated();
    }

    public boolean isActive() {
        return active;
    }

    protected boolean canStart() { return false; }

    @Override
    public void tick() {
        assert level != null;
        if (!level.isClientSide() && level.getDayTime() % 50 == 1) {
            if (!isActive() && canStart()) startMachine();
            if (active && started != 0L) {
                passed += (level.getDayTime() - started);
                started = level.getDayTime(); //Reset the time
                if (passed >= getOperationalTime()) {
                    active = false;
                    passed = 0L;
                    started = 0L;
                    finishMachine();
                    PenguinNetwork.sendToNearby(new SetActiveStatePacket(worldPosition, false), this);
                }
            }
        }
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        active = nbt.getBoolean("Active");
        started = nbt.getLong("Started");
        passed = nbt.getLong("Passed");
    }

    @Nonnull
    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putBoolean("Active", active);
        nbt.putLong("Started", started);
        nbt.putLong("Passed", passed);
        return super.save(nbt);
    }
}
