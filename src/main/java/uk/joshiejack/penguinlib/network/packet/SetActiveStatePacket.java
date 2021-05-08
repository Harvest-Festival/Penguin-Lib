package uk.joshiejack.penguinlib.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.tile.machine.AbstractMachineTileEntity;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SetActiveStatePacket extends PenguinPacket {
    private BlockPos pos;
    private boolean active;

    public SetActiveStatePacket() {}
    public SetActiveStatePacket(BlockPos pos, boolean active) {
        this.pos = pos;
        this.active = active;
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeBlockPos(pos);
        to.writeBoolean(active);
    }

    @Override
    public void decode(PacketBuffer from) {
        pos = from.readBlockPos();
        active = from.readBoolean();
    }

    @Override
    public void handle(PlayerEntity player) {
        TileEntity tile = player.level.getBlockEntity(pos);
        if (tile instanceof AbstractMachineTileEntity) {
            ((AbstractMachineTileEntity)tile).setState(active);
        }
    }
}
