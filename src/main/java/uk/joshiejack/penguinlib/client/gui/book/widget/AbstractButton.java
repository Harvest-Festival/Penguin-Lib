package uk.joshiejack.penguinlib.client.gui.book.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public abstract class AbstractButton extends Button {
    public AbstractButton(int x, int y, int w, int h, ITextComponent name, IPressable action, ITooltip tooltip) {
        super(x, y, w, h, name, action, tooltip);
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderButton(matrix, mouseX, mouseY, partialTicks, isHovered());
        if (this.isHovered()) {
            this.renderToolTip(matrix, mouseX, mouseY);
        }
    }

    protected abstract void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered);
}
