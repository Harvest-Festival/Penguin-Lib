package uk.joshiejack.penguinlib.events;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class BlockSmashedEvent extends PlayerEvent {
    private final BlockState state;
    private final BlockPos pos;
    private final Hand hand;

    public BlockSmashedEvent(PlayerEntity player, Hand hand, BlockPos pos, BlockState state) {
        super(player);
        this.state = state;
        this.pos = pos;
        this.hand = hand;
    }

    public BlockState getState() {
        return state;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Hand getHand() {
        return hand;
    }
}
