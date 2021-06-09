package uk.joshiejack.penguinlib.data.database;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.PenguinRegistries;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.events.DatabasePopulateEvent;
import uk.joshiejack.penguinlib.item.crafting.SimplePenguinRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID)
public class Database extends ReloadListener<Map<String, Table>> {
    public static final Database INSTANCE = new Database();
    private static final Logger LOGGER = LogManager.getLogger();
    public static final int pathSuffixLength = ".csv".length();
    private static final String directory = "database";
    private static final int dirLength = directory.length() + 1;
    public static boolean enableDebuggingTools = true;
    private final Map<String, String> tableData = new HashMap<>();

    public static class Dummy extends SimplePenguinRecipe {
        public static final ResourceLocation ALL = new ResourceLocation(PenguinLib.MODID, "all");

        public Dummy() {
            super(PenguinRegistries.DATABASE, PenguinRegistries.DATABASE_SERIALIZER.get(), ALL, Ingredient.EMPTY, ItemStack.EMPTY);
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<Database.Dummy> {
        @Nonnull
        @Override
        public Dummy fromJson(@Nonnull ResourceLocation rl, @Nonnull JsonObject json) {
            return new Dummy();
        }

        @Nullable
        @Override
        public Dummy fromNetwork(@Nonnull ResourceLocation rl, @Nonnull PacketBuffer packetbuffer) {
            Map<String, Table> tables = new HashMap<>();
            INSTANCE.tableData.clear(); //Refresh the data
            int tableCount = packetbuffer.readShort();
            for (int i = 0; i < tableCount; i++) {
                String name = packetbuffer.readUtf();
                int parts = packetbuffer.readShort();
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < parts; j++)
                    builder.append(packetbuffer.readUtf());
                parseCSV(tables, INSTANCE.tableData, name, builder.toString());
            }

            if (enableDebuggingTools)
                print(tables);
            MinecraftForge.EVENT_BUS.post(new DatabasePopulateEvent(tables));
            MinecraftForge.EVENT_BUS.post(new DatabaseLoadedEvent(tables));
            return new Dummy();
        }

        @Override
        public void toNetwork(@Nonnull PacketBuffer packetbuffer, @Nonnull Dummy dummy) {
            int tableCount = INSTANCE.tableData.size();
            packetbuffer.writeShort(tableCount);
            for (Map.Entry<String, String> entry: INSTANCE.tableData.entrySet()) {
                packetbuffer.writeUtf(entry.getKey());
                int parts = (int) Math.ceil((double)entry.getValue().length() / (double) Short.MAX_VALUE);
                packetbuffer.writeShort(parts);
                for (int j = 0; j < parts; j++)
                    packetbuffer.writeUtf(entry.getValue().substring(j * Short.MAX_VALUE, Math.min((j + 1) * Short.MAX_VALUE, entry.getValue().length())));
            }
        }
    }

    public static void print(Map<String, Table> tables) {
        for (String table: tables.keySet()) {
            LOGGER.info("############## TABLE: " + table +   " ##############");
            LOGGER.info(tables.get(table).labelset());

            tables.get(table).rows().forEach(r -> {
                List<String> arr = new ArrayList<>();
                tables.get(table).labels().forEach(header -> arr.add(r.get(header)));
                LOGGER.info(Arrays.toString(arr.toArray()));
            });
        }
    }

    @SubscribeEvent
    public static void registerData(AddReloadListenerEvent event) {
        event.addListener(new Database());
    }

    @Nonnull
    public <T> T get(Map<String, Table> tables, String search) {
        String[] terms = search.split(",");
        String table = terms[0].trim();
        String row = terms[1].trim();
        String data = terms[2].trim();
        Preconditions.checkNotNull(table, "The table cannot be null: " + table);
        Preconditions.checkNotNull(row, "The id to search in the table cannot be null: " + row);
        Preconditions.checkNotNull(data, "The instance you are searching for cannot be null: " + data);
        return tables.getOrDefault(table, Table.EMPTY).find(row).get(data);
    }

    public static Table createTable(Map<String, Table> tables, String name, String... labelset) {
        if (tables.containsKey(name)) return tables.get(name);
        else {
            Table table = new Table(name, labelset);
            tables.put(name, table);
            return table;
        }
    }

    private static void parseCSV(Map<String, Table> tables, Map<String, String> tableData, String name, String csv) {
        //Ignore any directories registered for this csv and just go with the name
        String file_name = new File(name).getName();
        name = (file_name.startsWith("$") ? file_name.replace("$", "") : file_name.contains("$") ? file_name.split("\\$")[1] : file_name).toLowerCase(Locale.ROOT); //Ignore anything before the dollar symbol
        tableData.put(name, csv);
        String[] entries = csv.split("[\\r\\n]+");
        String[] labels = entries[0].split(",");
        Table table = createTable(tables, name, labels); //Get a table with this name if it already exists
        for (int i = 1; i < entries.length; i++) {
            if (!entries[i].startsWith("#") && !entries[i].isEmpty()) {
                List<String> list = CSVUtils.parse(entries[i]);
                if (!list.isEmpty()) {
                    try {
                        table.insert(list.toArray(new String[0]));
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        LOGGER.log(Level.ERROR, "Failed to insert the csv: " + name + " as there was an issue on line: " + i + " " + entries[i]);
                    }
                }
            }
        }
    }

    private static void loadData(Map<String, Table> tables, IResourceManager rm, ResourceLocation rl) {
        String path = rl.getPath();
        try {
            IResource resource = rm.getResource(rl);
            InputStream is = resource.getInputStream();
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            parseCSV(tables, INSTANCE.tableData, path.substring(dirLength, path.length() - pathSuffixLength), IOUtils.toString(reader));
        } catch (IllegalArgumentException | IOException ex){
            LOGGER.error("Couldn't parse data file from {}", rl, ex);
        }
    }

    public static Table loadTable(@Nonnull IResourceManager rm, String table) {
        Map<String, Table> tables = new HashMap<>();
        rm.listResources(directory, (fileName) -> fileName.endsWith(table + ".csv")).forEach(rl -> loadData(tables, rm, rl));
        return tables.getOrDefault(table, Table.EMPTY);
    }

    @Nonnull
    @Override
    public Map<String, Table> prepare(@Nonnull IResourceManager rm, @Nonnull IProfiler profiler) {
        Map<String, Table> tables = new HashMap<>();
        tableData.clear(); //Remove the table data instance
        for (ResourceLocation rl : rm.listResources(directory, (fileName) -> fileName.endsWith(".csv"))) {
            loadData(tables, rm, rl);
        }

        return tables;
    }

    @Override
    protected void apply(@Nonnull Map<String, Table> tables, @Nonnull IResourceManager rm, @Nonnull IProfiler profiler) {
        if (enableDebuggingTools)
            print(tables);
        MinecraftForge.EVENT_BUS.post(new DatabasePopulateEvent(tables));
        MinecraftForge.EVENT_BUS.post(new DatabaseLoadedEvent(tables));
    }
}