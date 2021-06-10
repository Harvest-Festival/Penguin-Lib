package uk.joshiejack.penguinlib.client.gui.book.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.Book;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractButton extends Button {
    protected final Book book;

    public AbstractButton(Book book, int x, int y, int w, int h, ITextComponent name, IPressable action, ITooltip tooltip) {
        super(x, y, w, h, name, action, tooltip);
        this.book = book;
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderButton(matrix, mouseX, mouseY, partialTicks, isHovered());
        if (this.isHovered())
            book.addFuture(() -> renderToolTip(matrix, mouseX, mouseY));
    }

    protected abstract void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered);
}
