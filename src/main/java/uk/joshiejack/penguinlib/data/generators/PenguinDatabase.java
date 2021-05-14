package uk.joshiejack.penguinlib.data.generators;

import net.minecraft.data.DataGenerator;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.TimeUnitRegistry;
import uk.joshiejack.penguinlib.item.PenguinItems;

import java.util.Arrays;

public class PenguinDatabase extends AbstractDatabaseProvider {
    public PenguinDatabase(DataGenerator gen) {
        super(gen, PenguinLib.MODID);
    }

    @Override
    protected void addDatabaseEntries() {
        addFurnaceFuel(PenguinItems.DEEP_BOWL.get(), 150);
        Arrays.stream(TimeUnitRegistry.Defaults.values())
                .forEach(unit -> addTimeUnit(unit.getName(), unit.getValue()));
    }
}