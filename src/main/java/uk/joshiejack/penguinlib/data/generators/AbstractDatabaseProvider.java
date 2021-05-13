package uk.joshiejack.penguinlib.data.generators;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.data.TimeUnitRegistry;
import uk.joshiejack.penguinlib.data.database.CSVUtils;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class AbstractDatabaseProvider implements IDataProvider {
    private final Multimap<String, String> data = HashMultimap.create();
    private final Map<String, String> headings = new HashMap<>();
    private final DataGenerator gen;
    private final String modid;

    public AbstractDatabaseProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    public void addEntry(String file, String headings, String line) {
        this.headings.put(file, headings);
        this.data.get(file).add(line);
    }

    protected void addLootTableMerge(ResourceLocation target) {
        addEntry("merge_loot_table", "Target,Loot Table", CSVUtils.join(target, new ResourceLocation(modid, target.getPath())));
    }

    protected void addTimeUnitForMachine(TileEntityType<?> type, TimeUnitRegistry.Defaults duration) {
        addTimeUnit(Objects.requireNonNull(type.getRegistryName()).toString(), duration.getValue());
    }

    protected void addTimeUnitForMachine(TileEntityType<?> type, long duration) {
        addTimeUnit(Objects.requireNonNull(type.getRegistryName()).toString(), duration);
    }

    protected void addTimeUnit(String name, long duration) {
        addEntry("time_unit", "Name,Duration", CSVUtils.join(name, duration));
    }

    protected abstract void addDatabaseEntries();

    @Override
    public void run(DirectoryCache cache) throws IOException {
        addDatabaseEntries();
        if (!data.isEmpty())
            for (String file: data.keySet())
                save(cache, headings.get(file), data.get(file), gen.getOutputFolder().resolve("data/" + modid + "/database/" + file + ".csv"));
    }

    private void save(DirectoryCache cache, String headings, Collection<String> orig, Path target) throws IOException {
        StringBuilder builder = new StringBuilder(headings);
        List<String> strings = Lists.newArrayList(orig);
        Collections.sort(strings);
        strings.forEach(s -> { builder.append("\n"); builder.append(s); });
        String data = builder.toString();
        String hash = IDataProvider.SHA1.hashUnencodedChars(data).toString();
        if (!Objects.equals(cache.getHash(target), hash) || !Files.exists(target)) {
            Files.createDirectories(target.getParent());
            try (BufferedWriter bufferedwriter = Files.newBufferedWriter(target)) {
                bufferedwriter.write(data);
            }
        }

        cache.putNew(target, hash);
    }

    @Nonnull
    @Override
    public String getName() {
        return "CSV Database";
    }
}
