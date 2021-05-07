package uk.joshiejack.penguinlib.network.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SyncTeamDataPacket extends AbstractSyncCompoundNBTPacket {
    public SyncTeamDataPacket() {}
    public SyncTeamDataPacket(CompoundNBT data) {
        super(data);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClientPacket() {
        PenguinTeamsClient.setInstance(tag);
    }
}
