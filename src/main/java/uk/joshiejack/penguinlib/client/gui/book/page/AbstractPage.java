package uk.joshiejack.penguinlib.client.gui.book.page;

import com.mojang.blaze3d.systems.RenderSystem;
import joptsimple.internal.Strings;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.client.gui.book.tab.Tab;
import uk.joshiejack.penguinlib.client.gui.book.widget.TabButton;
import uk.joshiejack.penguinlib.util.Icon;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractPage {
    public static final ITextComponent EMPTY_STRING = new StringTextComponent(Strings.EMPTY);
    public static final AbstractPage EMPTY = new Basic(EMPTY_STRING);
    private final ITextComponent name;

    public AbstractPage(ITextComponent name) {
        this.name = name;
    }

    public AbstractButton createTab(Book book, Tab tab, int x, int y) {
        //Creates the tab for this page
        return new TabButton.Right(book, getIcon(), x, y, name, (btn) -> {
            tab.setPage(this);
            book.init(book.minecraft(), book.width, book.height);
        }, createTooltip(book, name), book.isSelected(this));
    }

    protected abstract Icon getIcon();

    public abstract void initLeft(Book book, int left, int top);

    public abstract void initRight(Book book, int left, int top);

    protected Button.ITooltip createTooltip(Book book, ITextComponent tooltip) {
        return (btn, mtx, mX, mY) -> {
            RenderSystem.disableDepthTest();
            book.renderTooltip(mtx,
                    book.minecraft().font.split(tooltip, Math.max(book.width / 2 - 43, 170)), mX, mY);
            RenderSystem.enableDepthTest();
        };
    }

    @OnlyIn(Dist.CLIENT)
    public static class Basic extends AbstractPage {
        public Basic(ITextComponent name) {
            super(name);
        }

        @Override
        protected Icon getIcon() {
            return Icon.ItemIcon.EMPTY;
        }

        @Override
        public void initLeft(Book book, int left, int top) {
        }

        @Override
        public void initRight(Book book, int left, int top) {
        }
    }
}
