package uk.joshiejack.penguinlib.item;

import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.PenguinLib;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID)
public class PenguinItems {
    public static final ItemEntity ENTITY = null;
    //public static final ItemSpecial SPECIAL = null;
    //public static final ItemDinnerware DINNERWARE = null;

    //TODO?
    /*
    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemEntity(), new ItemSpecial());
        if (PenguinConfig.forceDinnerwareItem || PenguinConfig.requireDishes) event.getRegistry().register(new ItemDinnerware());
        if (PenguinConfig.enableDebuggingTools) {
            event.getRegistry().register(new ItemTools());
        }
    }*/
}