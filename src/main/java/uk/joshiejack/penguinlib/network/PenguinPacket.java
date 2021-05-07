package uk.joshiejack.penguinlib.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess")
public abstract class PenguinPacket {
    public void encode(PacketBuffer pb) {}
    public void decode(PacketBuffer pb) {}
    public void handle(PlayerEntity player) {}

    public final void handle(Supplier<NetworkEvent.Context> ctx) {
        switch (ctx.get().getDirection()) {
            case PLAY_TO_CLIENT:
                ctx.get().enqueueWork(this::handleClientPacket);
                ctx.get().setPacketHandled(true); //Handled boyo
                break;
            case PLAY_TO_SERVER:
                ctx.get().enqueueWork(()-> handle(ctx.get().getSender()));
                ctx.get().setPacketHandled(true); //Handled boyo
                break;
            default:
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void handleClientPacket() {
        handle(Minecraft.getInstance().player);
    }
}
