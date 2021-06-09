package uk.joshiejack.penguinlib.data.custom;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

@SuppressWarnings("rawtypes")
public class CustomObject {
    public static final BiMap<String, Data> TYPE_REGISTRY = HashBiMap.create();
    public static final String UNNAMED = "unnamed";
    public String type;
    public String name;
    public Data<?> data;

    public CustomObject(){}
    public CustomObject(String type, Data<?> data) {
        this(type, UNNAMED, data);
    }

    public CustomObject(String type, String name, Data<?> data) {
        this.type = type;
        this.name = name;
        this.data = data;
    }

    public abstract static class Data<D> {
        public transient String type;

        public abstract void register(String name, D data);
    }
}