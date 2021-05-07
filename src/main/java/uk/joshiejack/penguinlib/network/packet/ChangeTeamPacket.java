package uk.joshiejack.penguinlib.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import java.util.UUID;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class ChangeTeamPacket extends PenguinPacket {
    private UUID player, oldTeam, newTeam;

    public ChangeTeamPacket() {}
    public ChangeTeamPacket(UUID player, UUID oldTeam, UUID newTeam) {
        this.player = player;
        this.oldTeam = oldTeam;
        this.newTeam = newTeam;
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeUUID(player);
        to.writeUUID(oldTeam);
        to.writeUUID(newTeam);
    }

    @Override
    public void decode(PacketBuffer from) {
        player = from.readUUID();
        oldTeam = from.readUUID();
        newTeam = from.readUUID();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClientPacket() {
        //TODO: PenguinTeamsClient.changeTeam(player, oldTeam, newTeam);
        //GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        //if (screen instanceof GuiBook) {
          //  ((GuiBook)screen).setPage(((GuiBook)screen).getPage());
        //}
    }
}
