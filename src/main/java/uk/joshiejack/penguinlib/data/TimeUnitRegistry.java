package uk.joshiejack.penguinlib.data;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID)
public class TimeUnitRegistry {
    private static final Object2LongMap<String> TIME_UNITS = new Object2LongOpenHashMap<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDatabaseLoaded(DatabaseLoadedEvent event) {
        TIME_UNITS.clear(); //Reset the time unit data
        event.table("time_unit").rows().forEach(row -> TIME_UNITS.put(row.name(), row.getAsLong("duration")));
    }

    public static long get(String name) {
        return TIME_UNITS.containsKey(name) ? TIME_UNITS.getLong(name) : Long.MAX_VALUE;
    }

    public enum Defaults {
        THREE_MINUTES(50), FIVE_MINUTES(100), QUARTER_HOUR(250),
        HALF_HOUR(500), HOUR(1000), HALF_DAY(12000), DAY(24000),
        WEEK(168000), YEAR(2880000)
        ;

        private final long time;

        Defaults(long time) {
            this.time = time;
        }

        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public long getValue() {
            return time;
        }
    }
}
