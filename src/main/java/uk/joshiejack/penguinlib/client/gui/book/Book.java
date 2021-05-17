package uk.joshiejack.penguinlib.client.gui.book;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import uk.joshiejack.penguinlib.client.gui.AbstractContainerScreen;
import uk.joshiejack.penguinlib.inventory.BookContainer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("ConstantConditions")
public class Book extends AbstractContainerScreen<BookContainer> {
    private static final Object2ObjectMap<String, Book> BOOK_INSTANCES = new Object2ObjectOpenHashMap<>();
    private final List<Tab> tabs = new ArrayList<>();
    private final ResourceLocation backgroundL;
    private final ResourceLocation backgroundR;
    private int centre, bgLeftOffset;
    private Tab defaultTab = Tab.EMPTY;
    private Tab tab;

    public Book(String modid, BookContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name, null, 360, 230);
        backgroundL = new ResourceLocation(modid, "textures/gui/book_left.png");
        backgroundR = new ResourceLocation(modid, "textures/gui/book_right.png");
    }

    public static Book getInstance(String modid, BookContainer container, PlayerInventory inv, ITextComponent name, Consumer<Book> consumer) {
        if (!BOOK_INSTANCES.containsKey(modid)) {
            Book screen = new Book(modid, container, inv, name);
            consumer.accept(screen); //Apply extra data to this bookscreen
            BOOK_INSTANCES.put(modid, screen);
        }

        return BOOK_INSTANCES.get(modid);
    }

    @Nonnull
    @Override
    public <T extends Widget> T addButton(@Nonnull T button) {
        return super.addButton(button);
    }

    public void setTab(Tab tab) {
        this.tab = tab;
        this.init(minecraft, width, height);
    }

    public void bindLeftTexture() {
        minecraft.getTextureManager().bind(backgroundL);
    }

    /**
     * Set the default page for this book
     **/
    public void withDefault(Tab tab) {
        this.defaultTab = tab;
    }

    /**
     * Add a page to this book, automatically creates a tab for the page on the left side of the book
     **/
    public Tab withTab(Tab tab) {
        if (!tabs.contains(tab))
            tabs.add(tab);
        return tab;
    }

    public Minecraft minecraft() {
        return minecraft;
    }

    public boolean isSelected(Page page) {
        return this.tab.getPage() == page;
    }

    public boolean isSelected(Tab tab) {
        return this.tab == tab;
    }

    @Override
    public void init() {
        super.init();
        centre = leftPos + (imageWidth / 2);
        bgLeftOffset = centre - 154;
        titleLabelX = (imageWidth / 2) - font.width(title) / 2;
        titleLabelY = topPos - 30;
        if (tab == null)
            tab = defaultTab;
        if (tabs.size() == 0)
            tabs.add(tab);
        int y = 0;
        for (Tab tab : tabs)
            addButton(tab.create(this, centre - 180, 15 + topPos + (y++ * 36)));
        tab.addTabs(this, centre + 154, 15 + topPos);
    }

    @Override
    protected void renderBg(@Nonnull MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
//        minecraft.getTextureManager().bind(backgroundL);
//        for (int j = 0; j <= imageHeight; j += 5)
//            for (int i = 0; i <= imageWidth; i += 5)
//                blit(matrix, leftPos + i, topPos + j, 0, 4, 1, 1);
        minecraft.getTextureManager().bind(backgroundL);
        blit(matrix, bgLeftOffset, topPos, 102, 0, 154, 202);
        minecraft.getTextureManager().bind(backgroundR);
        blit(matrix, centre, topPos, 0, 0, 154, 202);
    }

    @Override
    protected void renderLabels(@Nonnull MatrixStack matrix, int x, int y) {
        font.drawShadow(matrix, title, (float) titleLabelX, (float) titleLabelY, 0xFFFFFF);
    }
}