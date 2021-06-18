package uk.joshiejack.penguinlib.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class PenguinClientConfig {
    public static ForgeConfigSpec.EnumValue<ClockType> clockType;
    public static ForgeConfigSpec.BooleanValue enableDatabaseDebugger;

    PenguinClientConfig(ForgeConfigSpec.Builder builder) {
        clockType = builder.defineEnum("Clock Type", ClockType.TWENTY_FOUR_HOUR);
        enableDatabaseDebugger = builder.define("Enable Database Debug Output", false);
    }

    public static ForgeConfigSpec create() {
        return new ForgeConfigSpec.Builder().configure(PenguinClientConfig::new).getValue();
    }

    public enum ClockType {
        TWENTY_FOUR_HOUR, TWELVE_HOUR
    }
}
