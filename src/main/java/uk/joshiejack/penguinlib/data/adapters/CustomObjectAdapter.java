package uk.joshiejack.penguinlib.data.adapters;

import com.google.gson.*;
import uk.joshiejack.penguinlib.data.custom.CustomObject;

import java.lang.reflect.Type;


public class CustomObjectAdapter implements JsonSerializer<CustomObject>, JsonDeserializer<CustomObject> {
    @Override
    public JsonElement serialize(CustomObject src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        if (!src.name.equals(CustomObject.UNNAMED))
            result.addProperty("name", src.name);
        result.addProperty("type", src.type);
        result.add("data", context.serialize(src.data, CustomObject.TYPE_REGISTRY.get(src.type).getClass()).getAsJsonObject());
        return result;
    }

    @Override
    public CustomObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        CustomObject object = new CustomObject();
        object.name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : CustomObject.UNNAMED;
        object.type = jsonObject.get("type").getAsString();
        if (jsonObject.has("data")) {
            Class<?> clazz = CustomObject.TYPE_REGISTRY.get(object.type).getClass();
            object.data = context.deserialize(jsonObject.getAsJsonObject("data"), clazz);
        }

        return object;
    }
}

