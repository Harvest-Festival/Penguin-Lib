package uk.joshiejack.penguinlib.data.custom;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("rawtypes")
public class CustomObject {
    public static final BiMap<String, Data> TYPE_REGISTRY = HashBiMap.create();
    public static final ResourceLocation UNNAMED = new ResourceLocation("unnamed");
    public String type;
    public ResourceLocation name;
    public Data<?,?> data;

    public CustomObject(){}
    public CustomObject(String type, Data<?, ?> data) {
        this(type, UNNAMED, data);
    }

    public CustomObject(String type, ResourceLocation name, Data<?, ?> data) {
        this.type = type;
        this.name = name;
        this.data = data;
    }

    public abstract static class Data<B, D> {
        public transient String type;

        public abstract B build(ResourceLocation registryName, D data);
    }
}
