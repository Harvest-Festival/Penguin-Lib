package uk.joshiejack.penguinlib.data.custom;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class CustomObjectProvider implements IDataProvider {
    private final Map<String, Map<String, CustomObject>> data = new HashMap<>();
    private final DataGenerator gen;
    private final String modid;

    public CustomObjectProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    protected void addEntry(String file, String subdir, String type, ResourceLocation name, CustomObject.Data<?, ?> data) {
        Map<String, CustomObject> map = this.data.containsKey(subdir) ? this.data.get(subdir) : new HashMap<>();
        map.put(file, new CustomObject(type, name, data));
        this.data.put(subdir, map);
    }

    protected abstract void addCustomObjectEntries();

    @Override
    public void run(DirectoryCache cache) throws IOException {
        addCustomObjectEntries();
        if (!data.isEmpty())
            for (String subdir: data.keySet())
                for (String file: data.get(subdir).keySet())
                    save(cache, data.get(subdir).get(file), gen.getOutputFolder().resolve("data/" + modid + "/custom/" + subdir + "/" + file + ".json"));
    }

    private void save(DirectoryCache cache, CustomObject object, Path target) throws IOException {
        String data = PenguinGson.get().toJson(object);
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
