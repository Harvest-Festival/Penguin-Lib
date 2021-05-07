package uk.joshiejack.penguinlib.data.custom;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("rawtypes")
public class CustomObject {
    public static final BiMap<String, Data> TYPE_REGISTRY = HashBiMap.create();
    public static final ResourceLocation UNNAMED = new ResourceLocation("unnamed");
    public ResourceLocation name;
    public String type;
    public Data<?,?> data;

    public abstract static class Data<B, D> {
        public transient String type;

        public abstract B build(ResourceLocation registryName, D data);
    }
}
