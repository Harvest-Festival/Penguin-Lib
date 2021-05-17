package uk.joshiejack.penguinlib.inventory;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.PenguinLib;

public class PenguinContainers {
    //TODO: REMOVE
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, PenguinLib.MODID);
    public static final RegistryObject<ContainerType<BookContainer>> BOOK = CONTAINERS.register("book", () -> IForgeContainerType.create((id, inv, data) -> new BookContainer(id)));
}
