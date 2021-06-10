package uk.joshiejack.penguinlib.item;

import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.PenguinLib;

public class PenguinItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PenguinLib.MODID);
    public static final RegistryObject<Item> DEEP_BOWL = ITEMS.register("deep_bowl", () -> new Item(new Item.Properties().tab(PenguinLib.TAB)));
    public static final RegistryObject<Item> GLASS = ITEMS.register("glass", () -> new Item(new Item.Properties().tab(PenguinLib.TAB)));
    public static final RegistryObject<Item> JAM_JAR = ITEMS.register("jam_jar", () -> new Item(new Item.Properties().tab(PenguinLib.TAB)));
    public static final RegistryObject<Item> PICKLING_JAR = ITEMS.register("pickling_jar", () -> new Item(new Item.Properties().tab(PenguinLib.TAB)));
    public static final RegistryObject<Item> MUG = ITEMS.register("mug", () -> new Item(new Item.Properties().tab(PenguinLib.TAB)));
    public static final RegistryObject<Item> PLATE = ITEMS.register("plate", () -> new Item(new Item.Properties().tab(PenguinLib.TAB)));
    public static final RegistryObject<Item> UNFIRED_MUG = ITEMS.register("unfired_mug", () -> new Item(new Item.Properties().tab(PenguinLib.TAB)));
    public static final RegistryObject<Item> UNFIRED_PLATE = ITEMS.register("unfired_plate", () -> new Item(new Item.Properties().tab(PenguinLib.TAB)));
    public static final RegistryObject<Item> PENGUIN_BANNER_PATTERN = ITEMS.register("penguin_banner_pattern", () -> new BannerPatternItem(BannerPattern.create("PENGUIN", "penguin", "pgn", true), (new Item.Properties()).stacksTo(1).tab(PenguinLib.TAB)));
}