package uk.joshiejack.penguinlib.client.renderer.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.PenguinClient;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractItemBubbleTileEntityRenderer<T extends TileEntity> extends AbstractItemTileEntityRenderer<T> {
    public AbstractItemBubbleTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    protected double getYOffset() {
        return 1.5D;
    }

    protected void renderSpeechBubble(@Nonnull ItemStack stack, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        matrix.pushPose();
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer renderer = mc.getItemRenderer();
        matrix.translate(0.5D, getYOffset(), 0.5D);
        matrix.scale(0.75F, 0.75F, 0.75F);
        matrix.pushPose();
        matrix.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        matrix.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        RenderHelper.turnOff();
        RenderHelper.setupForFlatItems();
        matrix.pushPose();
        matrix.scale(0.75F, 0.75F, 0.01F);
        IBakedModel model = renderer.getModel(stack, mc.level, mc.player);
        renderer.render(stack, ItemCameraTransforms.TransformType.GUI, true, matrix, buffer, combinedLightIn, combinedOverlayIn, model);
        matrix.pushPose();
        matrix.scale(1.5F, 1.5F, 1.5F);
        matrix.translate(0F, -0.05F, -0.175F);
        IBakedModel model2 = Minecraft.getInstance().getModelManager().getModel(PenguinClient.SPEECH_BUBBLE);
        renderer.render(new ItemStack(Items.STICK), ItemCameraTransforms.TransformType.GUI, true, matrix, buffer, combinedLightIn, combinedOverlayIn, model2);
        matrix.popPose();
        matrix.popPose();
        RenderHelper.turnBackOn();
        matrix.popPose();
        matrix.popPose();
    }
}
