package uk.joshiejack.penguinlib.data;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootSerializers;
import net.minecraft.loot.LootTable;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.data.loot.AddLootGlobalModifier;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID)
public class LootTableMerger {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, PenguinLib.MODID);
    public static final RegistryObject<GlobalLootModifierSerializer<?>> ADD_ITEMS = LOOT_MODIFIER_SERIALIZERS.register("add_drop", AddLootGlobalModifier.Serializer::new);
    private static final Multimap<ResourceLocation, LootTable> resourceMap = HashMultimap.create();
    private static final Multimap<ResourceLocation, Pair<ResourceLocation, JsonElement>> elementMap = HashMultimap.create();
    private static final Gson GSON = LootSerializers.createLootTableSerializer().create();
    private static IResourceManager rm;
    // "Initial Loot table" > "Merging Loot table"

    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event) {
        rm = event.getDataPackRegistries().getResourceManager();
        resourceMap.clear();
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        //If we have not init the resource map then set it up
        if (resourceMap.size() == 0 && rm != null) {
            elementMap.clear();
            Database.loadTable(rm, "merge_loot_table").rows().forEach(row -> {
                ResourceLocation target = row.getRL("target");
                ResourceLocation lootTable = row.getRL("loot table");
                JsonElement element = build(target, lootTable, rm);
                if (!element.isJsonNull())
                    elementMap.get(target).add(Pair.of(lootTable, element));
            });

            elementMap.forEach((og, add) ->
                    resourceMap.put(og, ForgeHooks.loadLootTable(GSON, add.getLeft(), add.getRight(), true, event.getLootTableManager())));
            rm = null; //Clear out the resource manager instance now that we have used it
        }

        //Merge the pools over and under!
        List<LootPool> oldPools = ObfuscationReflectionHelper.getPrivateValue(LootTable.class, event.getTable(), "field_186466_c");
        resourceMap.get(event.getName()).forEach(lt -> {
            List<LootPool> newPools = ObfuscationReflectionHelper.getPrivateValue(LootTable.class, lt, "field_186466_c");
            assert oldPools != null;
            oldPools.forEach(lpO -> {
                List<LootEntry> oldEntries = ObfuscationReflectionHelper.getPrivateValue(LootPool.class, lpO, "field_186453_a");
                Objects.requireNonNull(newPools).forEach(lpN -> {
                    if (lpN.getName().equals(lpO.getName()) || (lpN.getName().startsWith("custom") && lpO.getName().equals("main"))) {
                        Objects.requireNonNull(oldEntries).addAll(Objects.requireNonNull(ObfuscationReflectionHelper.getPrivateValue(LootPool.class, lpN, "entries")));
                    }
                });
            });
        });
    }

    private static JsonElement build(ResourceLocation target, ResourceLocation loot_table, IResourceManager rm) {
        ResourceLocation location = new ResourceLocation(loot_table.getNamespace(), "loot_tables/" + loot_table.getPath() + ".json");
        try (IResource iresource = rm.getResource(location);
             InputStream inputstream = iresource.getInputStream();
             Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8))
        ) {
            JsonElement jsonelement = JSONUtils.fromJson(GSON, reader, JsonElement.class);
            return jsonelement == null || target == null ? new JsonObject() : jsonelement;
        } catch (IllegalArgumentException | IOException | JsonParseException jsonparseexception) {
            PenguinLib.LOGGER.error("Couldn't parse data file from {}", location, jsonparseexception);
        }

        return new JsonObject();
    }
}
