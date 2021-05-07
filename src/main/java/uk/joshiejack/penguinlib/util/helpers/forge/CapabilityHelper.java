package uk.joshiejack.penguinlib.util.helpers.forge;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityHelper {
    public static <E> LazyOptional<E> getCapabilityFromStack(ItemStack stack, Capability<E> e) {
        return stack.getCapability(e);
    }

    public static <E> LazyOptional<E> getCapabilityFromEntity(Entity entity, Capability<E> e) {
        return entity.getCapability(e);
    }
}
