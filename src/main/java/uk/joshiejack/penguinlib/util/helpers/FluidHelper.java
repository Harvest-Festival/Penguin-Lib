package uk.joshiejack.penguinlib.util.helpers;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class FluidHelper {
    public static int getFluidCapacityFromStack(ItemStack stack) {
        LazyOptional<IFluidHandlerItem> handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
        return handler.isPresent() ? handler.resolve().get().getTankCapacity(0) : 0;
    }

    public static boolean fillContainer(ItemStack stack, int maxWater) {
        LazyOptional<IFluidHandlerItem> handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
        return handler.isPresent() && handler.resolve().get().fill(new FluidStack(Fluids.WATER, maxWater), IFluidHandler.FluidAction.EXECUTE) > 0;
    }

    public static void drainContainer(ItemStack stack, int amount) {
        LazyOptional<IFluidHandlerItem> handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
        if (handler.isPresent())
            handler.ifPresent(container -> container.drain(amount, IFluidHandler.FluidAction.EXECUTE));
    }
}
