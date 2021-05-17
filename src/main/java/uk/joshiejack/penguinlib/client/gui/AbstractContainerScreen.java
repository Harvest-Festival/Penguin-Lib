package uk.joshiejack.penguinlib.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public abstract class AbstractContainerScreen <T extends Container> extends ContainerScreen<T> {
    protected final ResourceLocation background;

    //Allow for a null background, for special purposes
    public AbstractContainerScreen(T container, PlayerInventory inv, ITextComponent name, int width, int height) {
        this(container, inv, name, null, width, height);
    }

    public AbstractContainerScreen(T container, PlayerInventory inv, ITextComponent name, ResourceLocation background, int width, int height) {
        super(container, inv, name);
        this.background = background;
        this.imageWidth = width;
        this.imageHeight = height;
    }

    @Override
    protected void renderBg(@Nonnull MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
    }
}
