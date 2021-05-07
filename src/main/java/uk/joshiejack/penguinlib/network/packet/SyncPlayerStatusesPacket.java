package uk.joshiejack.penguinlib.network.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SyncPlayerStatusesPacket extends SyncPlayerNBTPacket {
    public SyncPlayerStatusesPacket() { super("PenguinStatuses"); }
    public SyncPlayerStatusesPacket(CompoundNBT tag) {
        super("PenguinStatuses", tag);
    }
}
