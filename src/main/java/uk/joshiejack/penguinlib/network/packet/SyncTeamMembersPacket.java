package uk.joshiejack.penguinlib.network.packet;

import com.google.common.collect.Maps;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SyncTeamMembersPacket extends PenguinPacket {
    private Map<UUID, UUID> memberOf;

    public SyncTeamMembersPacket() {}
    public SyncTeamMembersPacket(Map<UUID, UUID> memberOf) {
        this.memberOf = memberOf;
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeByte(memberOf.size());
        memberOf.forEach((key, value) -> {
            to.writeUUID(key);
            to.writeUUID(value);
        });
    }

    @Override
    public void decode(PacketBuffer from) {
        memberOf = Maps.newHashMap();
        int size = from.readByte();
        IntStream.range(0, size).forEach(i ->
                memberOf.put(from.readUUID(), from.readUUID()));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClientPacket() {
        PenguinTeamsClient.setMembers(memberOf);
    }
}
