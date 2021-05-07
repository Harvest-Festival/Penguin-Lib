package uk.joshiejack.penguinlib.network.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import uk.joshiejack.penguinlib.network.PenguinPacket;

public abstract class AbstractSyncCompoundNBTPacket extends PenguinPacket {
    protected CompoundNBT tag;

    public AbstractSyncCompoundNBTPacket() {}
    public AbstractSyncCompoundNBTPacket(CompoundNBT tag) {
        this.tag = tag;
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeNbt(tag);
    }

    @Override
    public void decode(PacketBuffer from) {
        tag = from.readNbt();
    }
}
