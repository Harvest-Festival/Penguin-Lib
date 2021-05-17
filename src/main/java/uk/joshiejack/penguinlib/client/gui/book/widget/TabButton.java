package uk.joshiejack.penguinlib.client.gui.book.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.ITextComponent;
import uk.joshiejack.penguinlib.client.gui.book.Book;

import javax.annotation.Nonnull;

public abstract class TabButton extends AbstractButton {
    protected final Book book;
    protected final boolean isSelected;

    public TabButton(Book book, int x, int y, ITextComponent name, IPressable action, ITooltip tooltip, boolean isSelected) {
        super(x, y, 26, 32, name, action, tooltip);
        this.isSelected = isSelected;
        this.book = book;
    }

    public static class Left extends TabButton {
        public Left(Book book, int x, int y, ITextComponent name, IPressable action, ITooltip tooltip, boolean isSelected) {
            super(book, x, y, name, action, tooltip, isSelected);
        }

        @Override
        protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
            book.bindLeftTexture();
            int yPos = 32 * (isSelected ? 1 : hovered ? 2: 0);
            blit(matrix, x, y, 26, yPos, width, height);
        }
    }

    public static class Right extends TabButton {
        public Right(Book book, int x, int y, ITextComponent name, IPressable action, ITooltip tooltip, boolean isSelected) {
            super(book, x, y, name, action, tooltip, isSelected);
        }

        @Override
        protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
            book.bindLeftTexture();
            int yPos = 32 * (isSelected ? 1 : hovered ? 2: 0);
            blit(matrix, x, y, 0, yPos, width, height);
        }
    }
}
