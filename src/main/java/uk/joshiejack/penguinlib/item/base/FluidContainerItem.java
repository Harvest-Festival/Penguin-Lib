package uk.joshiejack.penguinlib.item.base;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import java.util.function.Supplier;

public class FluidContainerItem extends PenguinItem {
    private final Supplier<Fluid> supplier;
    private final int volume;

    public FluidContainerItem(Supplier<Fluid> supplier, int volume, Item.Properties properties) {
        super(properties);
        this.supplier = supplier;
        this.volume = volume;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        FluidHandlerItemStack.Consumable consumable = new FluidHandlerItemStack.Consumable(stack, volume);
        consumable.fill(new FluidStack(supplier.get(), volume), IFluidHandler.FluidAction.EXECUTE);
        return consumable;
    }
}
