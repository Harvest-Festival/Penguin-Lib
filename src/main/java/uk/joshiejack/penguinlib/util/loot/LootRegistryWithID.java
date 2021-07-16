package uk.joshiejack.penguinlib.util.loot;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class LootRegistryWithID<I> extends LootRegistry<I> {
    private final Map<String, I> byID = new Object2ObjectOpenHashMap();

    public void add(String id, I value, double weight) {
        this.byID.put(id, value);
        this.add(value, weight);
    }

    @Override
    @Nonnull
    public I get(Random rand) {
        return Objects.requireNonNull(super.get(rand));
    }

    public I byID(String id) {
        return byID.get(id);
    }

    public boolean isSingleEntry() {
        return byID.size() == 1;
    }
}
