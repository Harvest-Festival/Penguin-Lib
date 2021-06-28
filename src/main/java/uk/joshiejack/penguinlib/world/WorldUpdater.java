package uk.joshiejack.penguinlib.world;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.NewDayEvent;
import uk.joshiejack.penguinlib.util.helpers.TimeHelper;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID)
public class WorldUpdater {
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (!event.world.isClientSide && event.phase == TickEvent.Phase.END && event.world.getDayTime() % TimeHelper.TICKS_PER_DAY == 1)
            MinecraftForge.EVENT_BUS.post(new NewDayEvent((ServerWorld) event.world)); //Post the new day event, to update
    }
}