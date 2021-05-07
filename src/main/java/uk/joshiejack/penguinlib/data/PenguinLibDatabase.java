package uk.joshiejack.penguinlib.data;

import net.minecraft.data.DataGenerator;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.penguinlib.data.database.DatabaseProvider;

public class PenguinLibDatabase extends DatabaseProvider {
    public PenguinLibDatabase(DataGenerator gen) {
        super(gen, PenguinLib.MODID);
    }

    protected void addTimeUnit(String name, int duration) {
        addEntry("time_unit", "Name,Duration", CSVUtils.join(name, duration));
    }

    @Override
    protected void addDatabaseEntries() {
        addTimeUnit("three_minutes", 50);
        addTimeUnit("five_minutes", 100);
        addTimeUnit("quarter_hour", 250);
        addTimeUnit("half_hour", 500);
        addTimeUnit("hour", 1000);
        addTimeUnit("half_day", 12000);
        addTimeUnit("day", 24000);
        addTimeUnit("week", 168000);
        addTimeUnit("year", 2880000);
    }
}