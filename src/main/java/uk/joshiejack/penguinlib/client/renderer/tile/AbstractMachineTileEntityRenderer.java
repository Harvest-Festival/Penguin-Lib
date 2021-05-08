package uk.joshiejack.penguinlib.client.renderer.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.tile.machine.AbstractMachineTileEntity;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractMachineTileEntityRenderer<T extends AbstractMachineTileEntity> extends AbstractItemTileEntityRenderer<T> {
    public AbstractMachineTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    public void render(@Nonnull T tile, float partialTicks, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        ItemStack inSlot = tile.getItem(0);
        if (tile.shouldRender(inSlot))
            renderSpeechBubble(inSlot, matrix, buffer, combinedLightIn, combinedOverlayIn);
    }
}
