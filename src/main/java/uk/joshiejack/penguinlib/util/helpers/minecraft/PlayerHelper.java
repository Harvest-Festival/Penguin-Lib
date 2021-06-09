package uk.joshiejack.penguinlib.util.helpers.minecraft;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class PlayerHelper {
    public static boolean hasTag(PlayerEntity player, String compoundTag, String tag) {
        return player.getPersistentData().getCompound(compoundTag).contains(tag);
    }

    public static void setTag(PlayerEntity player, String compoundTag, String tag) {
        if (!player.getPersistentData().contains(compoundTag)) {
            player.getPersistentData().put(compoundTag, new CompoundNBT());
        }

        player.getPersistentData().getCompound(compoundTag).putBoolean(tag, true);
    }
}