package uk.joshiejack.penguinlib.data.database.registries;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID)
public class TimeUnitRegistry {
    private static final Object2LongMap<String> TIME_UNITS = new Object2LongOpenHashMap<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDatabaseLoaded(DatabaseLoadedEvent event) {
        TIME_UNITS.clear(); //Reset the time unit data
        event.table("time_unit").rows().forEach(row -> TIME_UNITS.put(row.name(), row.getAsLong("duration")));
    }

    public static long get(String name) {
        return TIME_UNITS.getLong(name);
    }
}
