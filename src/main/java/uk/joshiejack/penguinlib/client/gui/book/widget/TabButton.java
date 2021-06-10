package uk.joshiejack.penguinlib.client.gui.book.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.util.Icon;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public abstract class TabButton extends AbstractButton {
    protected final Book book;
    protected final boolean isSelected;
    protected final Icon icon;

    public TabButton(Book book, Icon icon, int x, int y, ITextComponent name, IPressable action, ITooltip tooltip, boolean isSelected) {
        super(x, y, 26, 32, name, action, tooltip);
        this.isSelected = isSelected;
        this.book = book;
        this.icon = icon;
    }

    public static class Left extends TabButton {
        public Left(Book book, Icon icon, int x, int y, ITextComponent name, IPressable action, ITooltip tooltip, boolean isSelected) {
            super(book, icon, x, y, name, action, tooltip, isSelected);
        }

        @Override
        protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
            book.bindLeftTexture();
            int yPos = 32 * (isSelected ? 1 : hovered ? 2: 0);
            blit(matrix, x, y, 26, yPos, width, height);
            icon.render(Minecraft.getInstance(), matrix, x, y);
        }
    }

    public static class Right extends TabButton {
        public Right(Book book, Icon icon, int x, int y, ITextComponent name, IPressable action, ITooltip tooltip, boolean isSelected) {
            super(book, icon, x, y, name, action, tooltip, isSelected);
        }

        @Override
        protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
            book.bindLeftTexture();
            int yPos = 32 * (isSelected ? 1 : hovered ? 2: 0);
            blit(matrix, x, y, 0, yPos, width, height);
            icon.render(Minecraft.getInstance(), matrix, x, y + 8);
        }
    }
}
