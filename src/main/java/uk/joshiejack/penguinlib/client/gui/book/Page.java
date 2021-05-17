package uk.joshiejack.penguinlib.client.gui.book;

import joptsimple.internal.Strings;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import uk.joshiejack.penguinlib.client.gui.book.button.TabButton;

public class Page {
    public static final ITextComponent EMPTY_STRING = new StringTextComponent(Strings.EMPTY);
    public static final Page EMPTY = new Page(EMPTY_STRING);
    private final ITextComponent name;

    public Page(ITextComponent name) {
        this.name = name;
    }

    public AbstractButton createTab(Book screen, Tab tab, int x, int y) {
        Button.ITooltip tooltip = (btn, mtx, mX, mY) -> screen.renderTooltip(mtx,
                screen.minecraft().font.split(name, Math.max(screen.width / 2 - 43, 170)), mX, mY);
        Button.IPressable action = (btn) -> {
            tab.setPage(this);
            screen.init(screen.minecraft(), screen.width, screen.height);
        };

        //Creates the tab for this page
        return new TabButton.Right(screen, x, y, name, action, tooltip, screen.isSelected(this));
    }
}
