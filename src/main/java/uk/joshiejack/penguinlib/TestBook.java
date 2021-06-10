package uk.joshiejack.penguinlib;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.client.gui.book.tab.NotesTab;
import uk.joshiejack.penguinlib.inventory.AbstractBookContainer;
import uk.joshiejack.penguinlib.item.PenguinItems;
import uk.joshiejack.penguinlib.item.base.BookItem;
import uk.joshiejack.penguinlib.util.Icon;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TestBook {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, PenguinLib.MODID);
    public static final RegistryObject<ContainerType<AbstractBookContainer>> BOOK = CONTAINERS.register("test_book", () -> IForgeContainerType.create((id, inv, data) -> new TestContainer(id)));
    public static final RegistryObject<Item> TEST_BOOK = PenguinItems.ITEMS.register("test_book", () -> new BookItem(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_MISC), BOOK::get));

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        ScreenManager.register(TestBook.BOOK.get(), ((AbstractBookContainer container, PlayerInventory inv, ITextComponent text) ->
                Book.getInstance(PenguinLib.MODID, container, inv, text, (Book bs) -> {
                    ITextComponent name = new TranslationTextComponent("gui." + PenguinLib.MODID + ".book");
                    Icon icon = new Icon.TextureIcon(Icon.DEFAULT_LOCATION, 0, 0);
                    bs.withTab(new NotesTab(name, icon));
                })
        ));

    }

    public static class TestContainer extends AbstractBookContainer {
        public TestContainer(int windowID) { super(TestBook.BOOK.get(), windowID); }
    }
}