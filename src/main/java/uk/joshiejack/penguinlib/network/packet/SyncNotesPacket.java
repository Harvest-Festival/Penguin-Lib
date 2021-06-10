package uk.joshiejack.penguinlib.network.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SyncNotesPacket extends SyncPlayerNBTPacket {
    public SyncNotesPacket() { super("Notes"); }
    public SyncNotesPacket(CompoundNBT tag) {
        super("Notes", tag);
    }
}