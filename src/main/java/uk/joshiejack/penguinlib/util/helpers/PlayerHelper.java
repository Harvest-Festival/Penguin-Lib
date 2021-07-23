package uk.joshiejack.penguinlib.util.helpers;

import com.google.common.collect.Streams;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class PlayerHelper {
    private static CompoundNBT getOrCreateTag(CompoundNBT base, String name) {
        if (!base.contains(name))
            base.put(name, new CompoundNBT());
        return base.getCompound(name);
    }

    public static boolean hasTag(PlayerEntity player, String compoundTag, String tag) {
        return getOrCreateTag(player.getPersistentData(), compoundTag).contains(tag);
    }

    public static void setTag(PlayerEntity player, String compoundTag, String tag) {
        getOrCreateTag(player.getPersistentData(), compoundTag).putBoolean(tag, true);
    }

    public static void setSubTag(PlayerEntity player, String main, String sub, String tag) {
        getOrCreateTag(getOrCreateTag(player.getPersistentData(), main), sub).putBoolean(tag, true);
    }

    public static boolean hasSubTag(PlayerEntity player, String main, String sub, String tag) {
        return getOrCreateTag(getOrCreateTag(player.getPersistentData(), main), sub).contains(tag);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Stream<ItemStack> getInventoryStream(PlayerEntity player) {
        return Streams.concat(player.inventory.items.stream(), player.inventory.armor.stream(), player.inventory.offhand.stream());
    }

    public static boolean hasInInventory(PlayerEntity player, Item item, int amount) {
        return hasInInventory(player, (stack) -> stack.getItem() == item, amount);
    }

    public static boolean hasInInventory(PlayerEntity player, ITag.INamedTag<Item> tag, int amount) {
        return hasInInventory(player, (stack) -> stack.getItem().is(tag), amount);
    }

    public static boolean hasInInventory(PlayerEntity player, Predicate<ItemStack> predicate, int amount) {
        return getInventoryStream(player).mapToInt(stack -> predicate.test(stack) ? stack.getCount() : 0).sum() >= amount;
    }

    public static boolean takeFromInventory(PlayerEntity player, Item item, int amount) {
        return takeFromInventory(player, (stack) -> stack.getItem() == item, amount);
    }

    public static boolean takeFromInventory(PlayerEntity player, ITag.INamedTag<Item> tag, int amount) {
        return takeFromInventory(player, (stack) -> stack.getItem().is(tag), amount);
    }

    public static boolean takeFromInventory(PlayerEntity player, Predicate<ItemStack> predicate, int amount) {
        MutableInt taken = new MutableInt(amount);
        return getInventoryStream(player).anyMatch(stack -> {
            if (predicate.test(stack)) {
                int take = Math.min(stack.getCount(), taken.intValue());
                stack.shrink(take);
                taken.subtract(take);
            }

            return taken.intValue() == 0;
        });
    }

    public static CompoundNBT getPenguinStatuses(PlayerEntity player) {
        CompoundNBT data = player.getPersistentData();
        if (!data.contains("PenguinStatuses"))
            data.put("PenguinStatuses", new CompoundNBT());
        return data.getCompound("PenguinStatuses");
    }
}