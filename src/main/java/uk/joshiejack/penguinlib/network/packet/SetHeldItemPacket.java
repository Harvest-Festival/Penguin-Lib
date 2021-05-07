package uk.joshiejack.penguinlib.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SetHeldItemPacket extends PenguinPacket {
    private Hand hand;
    private ItemStack stack;

    public SetHeldItemPacket() {}
    public SetHeldItemPacket(Hand hand, ItemStack stack) {
        this.hand = hand;
        this.stack = stack;
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeByte(hand.ordinal());
        to.writeItemStack(stack, false);
    }

    @Override
    public void decode(PacketBuffer from) {
        hand = Hand.values()[from.readByte()];
        stack = from.readItem();
    }

    @Override
    public void handle(PlayerEntity player) {
        player.setItemInHand(hand, stack);
    }
}
