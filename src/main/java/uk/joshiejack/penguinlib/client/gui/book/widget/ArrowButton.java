package uk.joshiejack.penguinlib.client.gui.book.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.gui.book.Book;

import javax.annotation.Nonnull;

public abstract class ArrowButton extends AbstractButton {
    protected final Book book;

    public ArrowButton(Book book, int x, int y, ITextComponent name, IPressable action) {
        super(x, y, 15, 10, name, action, (btn, mtx, mX, mY) -> {
            book.renderTooltip(mtx,
                    book.minecraft().font.split(name, Math.max(book.width / 2 - 43, 170)), mX, mY);
        });
        this.book = book;
    }

    public static class Left extends ArrowButton {
        public Left(Book book, int x, int y, IPressable action) {
            super(book, x, y, new TranslationTextComponent("button." + PenguinLib.MODID + ".previous"), action);
        }

        @Override
        protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
            book.bindLeftTexture();
            int yPos = 11 * (hovered ? 1: 0);
            blit(matrix, x, y, 16, 235 + yPos, width, height);
        }
    }

    public static class Right extends ArrowButton {
        public Right(Book book, int x, int y, IPressable action) {
            super(book, x, y, new TranslationTextComponent("button." + PenguinLib.MODID + ".next"), action);
        }

        @Override
        protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
            book.bindLeftTexture();
            int yPos = 11 * (hovered ? 1: 0);
            blit(matrix, x, y, 0, 235 + yPos, width, height);
        }
    }
}
