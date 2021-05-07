package uk.joshiejack.penguinlib.events;


import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;

import java.util.Map;
import java.util.function.BiConsumer;

public class CollectRegistryEvent<R> extends Event implements IModBusEvent {
    protected final Map<Class<?>, BiConsumer<Class<?>, String>> processors;

    protected CollectRegistryEvent(Map<Class<?>, BiConsumer<Class<?>, String>> processors) {
        this.processors = processors;
    }

    public static class Loader extends CollectRegistryEvent<String> {
        public Loader(Map<Class<?>, BiConsumer<Class<?>, String>> processors) {
            super(processors);
        }

        @SuppressWarnings("unchecked, rawtypes")
        public <T> void add(Class<T> clazz, BiConsumer<Class<T>, String> r) {
            processors.put(clazz, (BiConsumer) r);
        }
    }
}