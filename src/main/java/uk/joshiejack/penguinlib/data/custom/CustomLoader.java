package uk.joshiejack.penguinlib.data.custom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.PenguinGson;
import uk.joshiejack.penguinlib.data.custom.loot_tables.LootTableMerge;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class CustomLoader {
    private static final int PATH_SUFFIX_LENGTH = ".json".length();

    @SuppressWarnings("unchecked")
    private static <T> T build(CustomObject co) {
        return (T) CustomObject.TYPE_REGISTRY.get(co.type).build(co.name, co.data);
    }

    @SuppressWarnings("unchecked")
    public static <I extends CustomObject> List<I> loadJson(IResourceManager rm, String dir) {
        List<I> list = new ArrayList<>();
        for (ResourceLocation rl: rm.listResources(dir, (fileName) -> fileName.endsWith(".json"))) {
            try {
                IResource resource = rm.getResource(rl);
                InputStream is = resource.getInputStream();
                Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                I i = (I) PenguinGson.get().fromJson(IOUtils.toString(reader), CustomObject.class);
                if (i != null)
                    list.add(i);
                else PenguinLib.LOGGER.error("FAILED TO LOAD " + rl);
            } catch (IllegalArgumentException | IOException ex){
                PenguinLib.LOGGER.error("Couldn't kill data file from {}", rl, ex);
            }
        }

        return list;
    }

    @Mod.EventBusSubscriber(modid = PenguinLib.MODID)
    public static class LootTables extends ReloadListener<Map<ResourceLocation, Pair<ResourceLocation, JsonElement>>> {
        private static final Multimap<ResourceLocation, LootTable> resourceMap = HashMultimap.create();
        private static final Multimap<ResourceLocation, Pair<ResourceLocation, JsonElement>> elementMap = HashMultimap.create();
        // "Initial Loot table" > "Merging Loot table"

        @SubscribeEvent
        public static void onReload(AddReloadListenerEvent event) {
            event.addListener(new LootTables());
        }

        @SubscribeEvent
        public static void onLootTableLoad(LootTableLoadEvent event) {
            if (resourceMap.size() == 0) {
                elementMap.forEach((og, add) -> {
                    resourceMap.put(og, ForgeHooks.loadLootTable(LootTableMerge.GSON, add.getLeft(), add.getRight(), true, event.getLootTableManager()));
                });
            }

            List<LootPool> oldPools = ObfuscationReflectionHelper.getPrivateValue(LootTable.class, event.getTable(), "pools");
            resourceMap.get(event.getName()).forEach(lt -> {
                List<LootPool> newPools = ObfuscationReflectionHelper.getPrivateValue(LootTable.class, lt, "pools");
                assert oldPools != null;
                oldPools.forEach(lpO -> {
                    List<LootEntry> oldEntries = ObfuscationReflectionHelper.getPrivateValue(LootPool.class, lpO, "entries");
                    Objects.requireNonNull(newPools).forEach(lpN -> {
                        if (lpN.getName().equals(lpO.getName()) || (lpN.getName().startsWith("custom") && lpO.getName().equals("main"))) {
                            Objects.requireNonNull(oldEntries).addAll(Objects.requireNonNull(ObfuscationReflectionHelper.getPrivateValue(LootPool.class, lpN, "entries")));
                        }
                    });
                });
            });
        }

        @Override
        protected Map<ResourceLocation, Pair<ResourceLocation, JsonElement>> prepare(IResourceManager rm, IProfiler profiler) {
            return CustomLoader.loadJson(rm, "custom/loot_tables").stream().map(co -> (LootTableMerge)co.data)
            .collect(Collectors.toMap(ltm -> ltm.original, ltm -> Pair.of(ltm.addition, ltm.build(null, rm))));
        }

        @Override
        protected void apply(Map<ResourceLocation, Pair<ResourceLocation, JsonElement>> map, IResourceManager rm, IProfiler profiler) {
            resourceMap.clear();
            map.entrySet().stream().filter(entry -> !entry.getValue().getRight().isJsonNull())
            .forEach((e) -> elementMap.get(e.getKey()).add(e.getValue()));
        }

        protected ResourceLocation getPreparedPath(ResourceLocation rl) {
            return new ResourceLocation(rl.getNamespace(), "custom/loot_tables/" + rl.getPath() + ".json");
        }
    }
}
