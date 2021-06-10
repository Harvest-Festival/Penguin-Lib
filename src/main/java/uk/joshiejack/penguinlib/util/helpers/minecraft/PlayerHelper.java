package uk.joshiejack.penguinlib.util.helpers.minecraft;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

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
}