package uk.joshiejack.penguinlib.util.helpers;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.PenguinLib;

import java.time.DayOfWeek;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID)
public class TimeHelper {
    public static long TICKS_PER_DAY = 24000L;
    public static double SCALE = TICKS_PER_DAY / 24000D;
    public static long SIX_AM = (long) (SCALE * 6000D);
    public static final DayOfWeek[] DAYS = DayOfWeek.values();

    public static long scaleTime(long time) {
        return (long) (SCALE * (double) time);
    }

    public static int getElapsedDays(long time) {
        return (int) (time / TICKS_PER_DAY);
    }

    public static int getElapsedDays(World world) {
        return (int) (world.getGameTime() / TICKS_PER_DAY);
    }

    public static long getTimeOfDay(long time) {
        return (time + SIX_AM) % TICKS_PER_DAY;
    }

    public static boolean isBetween(World world, int open, int close) {
        long timeOfDay = getTimeOfDay(world.getGameTime()); //0-23999 by default
        return timeOfDay >= open && timeOfDay <= close;
    }

    public static DayOfWeek getWeekday(long time) {
        int days = TimeHelper.getElapsedDays(time);
        int modulus = days % 7;
        if (modulus < 0) modulus = 0;
        return DAYS[modulus];
    }

    public static String shortName(DayOfWeek day) {
        return day.name().substring(0, 3);
    }
}
