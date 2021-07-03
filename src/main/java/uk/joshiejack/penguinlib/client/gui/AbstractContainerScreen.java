package uk.joshiejack.penguinlib.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractContainerScreen <T extends Container> extends ContainerScreen<T> {
    protected final ResourceLocation background;
    private final List<Runnable> futures = new ArrayList<>();

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

    public void addFuture(Runnable r) {
        futures.add(r);
    }

    @Override
    public void render(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.futures.forEach(Runnable::run);
        this.futures.clear();
    }
}
