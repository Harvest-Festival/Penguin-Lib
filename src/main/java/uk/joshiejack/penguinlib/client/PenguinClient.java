package uk.joshiejack.penguinlib.client;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.client.gui.book.Page;
import uk.joshiejack.penguinlib.client.gui.book.Tab;
import uk.joshiejack.penguinlib.inventory.BookContainer;
import uk.joshiejack.penguinlib.inventory.PenguinContainers;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = PenguinLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PenguinClient {
    public static final ResourceLocation SPEECH_BUBBLE = new ResourceLocation(PenguinLib.MODID, "extra/speech_bubble");

    @SubscribeEvent
    public static void loadModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(SPEECH_BUBBLE);
    }

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        ScreenManager.register(PenguinContainers.BOOK.get(), ((BookContainer container, PlayerInventory inv, ITextComponent text) ->
                Book.getInstance(PenguinLib.MODID, container, inv, text, (Book bs) -> {
                    bs.withDefault(Tab.EMPTY);
                    bs.withTab(Tab.EMPTY);
                    Tab.EMPTY.withDefault(Page.EMPTY);

                    Tab t1 = new Tab(new StringTextComponent("TEST"));
                    Page p1 = new Page(new StringTextComponent("rx8"));
                    Page p2 = new Page(new StringTextComponent("490ti"));
                    bs.withTab(t1).withDefault(p2).withPage(p1).withPage(p2);
                })));
    }
}