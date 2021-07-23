package uk.joshiejack.penguinlib.util.helpers;

import com.google.common.collect.Streams;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.mutable.MutableInt;

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

    public static boolean hasInInventory(PlayerEntity player, Ingredient ingredient, int amount) {
        return getInventoryStream(player).mapToInt(stack -> ingredient.test(stack) ? stack.getCount() : 0).sum() >= amount;
    }

    public static boolean hasInInventory(PlayerEntity player, ITag.INamedTag<Item> tag, int amount) {
        return getInventoryStream(player).mapToInt(stack -> stack.getItem().is(tag) ? stack.getCount() : 0).sum() >= amount;
    }
    
    public static boolean takeFromInventory(PlayerEntity player, Ingredient ingredient, int amount) {
        MutableInt taken = new MutableInt(amount);
        return getInventoryStream(player).anyMatch(stack -> {
            if (ingredient.test(stack)) {
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