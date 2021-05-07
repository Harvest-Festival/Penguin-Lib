package uk.joshiejack.penguinlib.events;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;

public class NewDayEvent extends WorldEvent {
    public NewDayEvent(ServerWorld world) {
        super(world);
    }

    @Override
    public ServerWorld getWorld() {
        return (ServerWorld) super.getWorld();
    }
}
