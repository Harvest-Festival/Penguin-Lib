package uk.joshiejack.penguinlib.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.inventory.PenguinContainers;
import uk.joshiejack.penguinlib.item.base.BookItem;

public class PenguinItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PenguinLib.MODID);
    public static final RegistryObject<Item> DEEP_BOWL = ITEMS.register("deep_bowl", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> GLASS = ITEMS.register("glass", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> JAM_JAR = ITEMS.register("jam_jar", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> PICKLING_JAR = ITEMS.register("pickling_jar", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> MUG = ITEMS.register("mug", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> PLATE = ITEMS.register("plate", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> UNFIRED_MUG = ITEMS.register("unfired_mug", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> UNFIRED_PLATE = ITEMS.register("unfired_plate", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> TEST_BOOK = ITEMS.register("test_book", () -> new BookItem(new Item.Properties().tab(ItemGroup.TAB_MISC), PenguinContainers.BOOK::get));
}