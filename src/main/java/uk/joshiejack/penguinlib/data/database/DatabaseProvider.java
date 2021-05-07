package uk.joshiejack.penguinlib.data.database;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class DatabaseProvider implements IDataProvider {
    private final Multimap<String, String> data = HashMultimap.create();
    private final Map<String, String> headings = new HashMap<>();
    private final DataGenerator gen;
    private final String modid;

    public DatabaseProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    protected void addEntry(String file, String headings, String line) {
        this.headings.put(file, headings);
        this.data.get(file).add(line);
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
