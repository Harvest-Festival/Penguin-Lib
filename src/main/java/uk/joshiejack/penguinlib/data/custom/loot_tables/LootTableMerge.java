package uk.joshiejack.penguinlib.data.custom.loot_tables;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.loot.LootSerializers;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.custom.CustomObject;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;

@PenguinLoader("loot_table:merge")
public class LootTableMerge extends CustomObject.Data<JsonElement, IResourceManager> {
    public static final Gson GSON = LootSerializers.createLootTableSerializer().create();
    public ResourceLocation original;
    public ResourceLocation addition;

    @Override
    public JsonElement build(@Nullable ResourceLocation registryName, IResourceManager rm) {
        ResourceLocation location = new ResourceLocation(addition.getNamespace(), "loot_tables/" + addition.getPath() + ".json");
        try (IResource iresource = rm.getResource(location);
             InputStream inputstream = iresource.getInputStream();
             Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8))
        ) {
            JsonElement jsonelement = JSONUtils.fromJson(GSON, reader, JsonElement.class);
            return jsonelement == null || original == null || addition == null ? new JsonObject() : jsonelement;
        } catch (IllegalArgumentException | IOException | JsonParseException jsonparseexception) {
            PenguinLib.LOGGER.error("Couldn't parse data file from {}", location, jsonparseexception);
        }

        return new JsonObject();
    }


}
