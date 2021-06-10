package uk.joshiejack.penguinlib.client.gui.book.tab;

import com.mojang.blaze3d.systems.RenderSystem;
import joptsimple.internal.Strings;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.client.gui.book.page.AbstractPage;
import uk.joshiejack.penguinlib.client.gui.book.widget.TabButton;
import uk.joshiejack.penguinlib.util.Icon;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class Tab {
    public static final ITextComponent EMPTY_STRING = new StringTextComponent(Strings.EMPTY);
    public static final Tab EMPTY = new Tab(EMPTY_STRING, Icon.ItemIcon.EMPTY);
    private final List<AbstractPage> pages = new ArrayList<>();
    private final ITextComponent name;
    private final Icon icon;
    private AbstractPage defaultPage = AbstractPage.EMPTY;
    private AbstractPage page;

    public Tab(ITextComponent name, Icon icon) {
        this.name = name;
        this.icon = icon;
    }

    protected List<AbstractPage> getPages() {
        return pages;
    }

    public Tab withPage(AbstractPage page) {
        if (!pages.contains(page))
            pages.add(page);
        if (defaultPage == AbstractPage.EMPTY)
            defaultPage = page;
        return this;
    }

    protected Icon getIcon() {
        return icon;
    }

    public AbstractPage getPage() {
        if (page == null)
            page = defaultPage;
        return page;
    }

    public void setPage(AbstractPage page) {
        this.page = page;
    }

    protected Button.ITooltip createTooltip(Book book, ITextComponent tooltip) {
        return (btn, mtx, mX, mY) -> {
            RenderSystem.disableDepthTest();
            book.renderTooltip(mtx,
                    book.minecraft().font.split(tooltip, Math.max(book.width / 2 - 43, 170)), mX, mY);
            RenderSystem.enableDepthTest();
        };
    }

    public AbstractButton create(Book book, int x, int y) {
        //Creates the tab for this page
        return new TabButton.Left(book, getIcon(), x, y, name, (btn) -> {
            if (page == null)
                page = defaultPage;
            book.setTab(this); //Refresh
        }, createTooltip(book, name), book.isSelected(this));
    }

    public void addTabs(Book screen, int x, int y) {
        List<AbstractPage> pages = getPages();
        int i = 0;
        for (AbstractPage page : pages)
            screen.addButton(page.createTab(screen, this, x, y + (i++ * 36)));
    }
}
