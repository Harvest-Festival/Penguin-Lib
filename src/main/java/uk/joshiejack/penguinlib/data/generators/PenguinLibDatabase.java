package uk.joshiejack.penguinlib.data.generators;

import net.minecraft.data.DataGenerator;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.TimeUnitRegistry;

import java.util.Arrays;

public class PenguinLibDatabase extends AbstractDatabaseProvider {
    public PenguinLibDatabase(DataGenerator gen) {
        super(gen, PenguinLib.MODID);
    }

    @Override
    protected void addDatabaseEntries() {
        Arrays.stream(TimeUnitRegistry.Defaults.values())
                .forEach(unit -> addTimeUnit(unit.getName(), unit.getValue()));
    }
}