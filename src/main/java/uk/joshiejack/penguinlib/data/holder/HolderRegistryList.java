package uk.joshiejack.penguinlib.data.holder;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class HolderRegistryList {
    private final List<Item> list = Lists.newArrayList();

    public void add(Item holder) {
        if (holder != null) {
            list.add(holder);
        }
    }

    public void addAll(Collection<Item> holders) {
        list.addAll(holders);
    }

    public boolean contains(Item holder) {
        return list.contains(holder);
    }

    public boolean contains(ItemStack stack) {
        return list.contains(stack.getItem());
    }

    public void clear() {
        list.clear();
    }

    public List<Item> getList() {
        return list;
    }
}
