package uk.joshiejack.penguinlib.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.data.PenguinRegistries;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.note.Note;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class UnlockNotePacket extends PenguinPacket {
    private ResourceLocation note;

    public UnlockNotePacket(){}
    public UnlockNotePacket(Note note) {
        this.note = note.getId();
    }

    @Override
    public void encode(PacketBuffer pb) {
        pb.writeResourceLocation(note);
    }

    @Override
    public void decode(PacketBuffer pb) {
        note = pb.readResourceLocation();
    }

    @Override
    public void handle(PlayerEntity player) {
        ((Note)player.level.getRecipeManager().recipes.get(PenguinRegistries.NOTE).get(note)).unlock(player);
    }
}