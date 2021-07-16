package uk.joshiejack.penguinlib.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.AbstractContainerScreen;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractButton<S extends AbstractContainerScreen<?>> extends Button {
    protected final S screen;
    protected final Minecraft mc;

    public AbstractButton(S screen, int x, int y, int w, int h, ITextComponent name, IPressable action, ITooltip tooltip) {
        super(x, y, w, h, name, action, tooltip);
        this.screen = screen;
        this.mc = screen.getMinecraft();
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderButton(matrix, mouseX, mouseY, partialTicks, isHovered());
        if (this.isHovered())
            screen.addFuture(() -> renderToolTip(matrix, mouseX, mouseY));
    }

    protected abstract void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered);
}