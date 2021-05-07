package uk.joshiejack.penguinlib.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class BlockRenderUpdatePacket extends PenguinPacket {
    protected BlockPos pos;

    public BlockRenderUpdatePacket() {}
    public BlockRenderUpdatePacket(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeLong(pos.asLong());
    }

    @Override
    public void decode(PacketBuffer from) {
        pos = BlockPos.of(from.readLong());
    }

    @Override
    public void handle(PlayerEntity player) {
        //TODO: player.level.markBlockRangeForRenderUpdate(pos, pos);
    }
}
