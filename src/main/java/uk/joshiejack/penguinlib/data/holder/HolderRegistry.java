package uk.joshiejack.penguinlib.data.holder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class HolderRegistry<T> {
    private static final Random rand = new Random(System.currentTimeMillis());
    private final LinkedHashMap<Item, T> map = Maps.newLinkedHashMap();
    private final T null_value;

    public void register(Item holder, T t) {
        map.put(holder, t);
    }

    public HolderRegistry(T null_value) {
        this.null_value = null_value;
    }

    @Nonnull
    public T getValue(ItemStack stack) {
        return map.getOrDefault(stack.getItem(), null_value);
    }

    public Item random(T type) {
        List<Item> t = map.entrySet().stream()
                .filter(t1 -> t1.getValue().equals(type))
                .map(Map.Entry::getKey).collect(Collectors.toList());
        return t.get(rand.nextInt(t.size())); //
    }

    public Set<Map.Entry<Item, T>> getEntries()  {
        return map.entrySet();
    }

    public List<T> values() {
        return Lists.newArrayList(map.values());
    }

    public List<Item> getKeys(T t) {
        List<Item> holders = Lists.newArrayList();
        for (Map.Entry<Item, T> entry: map.entrySet()) {
            if (t == entry.getValue()) holders.add(entry.getKey());
        }

        return holders;
    }

    public void clear() {
        map.clear();
    }
}
