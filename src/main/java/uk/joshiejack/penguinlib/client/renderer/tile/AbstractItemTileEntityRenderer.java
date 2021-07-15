package uk.joshiejack.penguinlib.client.renderer.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import uk.joshiejack.penguinlib.client.PenguinClient;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractItemTileEntityRenderer<T extends TileEntity> extends TileEntityRenderer<T> {
    private static final Supplier<ItemStack> STICK = () -> new ItemStack(Items.STICK);
    private static Consumer<MatrixStack> DEFAULT = (matrixStack -> matrixStack.scale(0.5F, 0.5F, 0.5F));
    private static ItemStack stack;

    public AbstractItemTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Nonnull
    protected ItemStack getStick() {
        return stack == null ? stack = STICK.get() : stack;
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
        renderer.render(getStick(), ItemCameraTransforms.TransformType.GUI, true, matrix, buffer, combinedLightIn, combinedOverlayIn, model2);
        matrix.popPose();
        matrix.popPose();
        RenderHelper.turnBackOn();
        matrix.popPose();
        matrix.popPose();
    }

    protected void renderItem(Minecraft mc, ItemStack stack, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        renderItem(mc, stack, matrix, buffer, combinedLightIn, combinedOverlayIn, DEFAULT);
    }

    protected void renderItem(Minecraft mc, ItemStack stack, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn, Consumer<MatrixStack> transforms) {
        matrix.pushPose();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        if (!stack.isEmpty()) {
            matrix.translate(0.5F, 1F, 0.5F);
            matrix.pushPose();
            transforms.accept(matrix);
            RenderHelper.setupFor3DItems();
            itemRenderer.render(stack, ItemCameraTransforms.TransformType.FIXED, true, matrix, buffer,
                    combinedLightIn, combinedOverlayIn, itemRenderer.getModel(stack, mc.level, null));
            RenderHelper.turnOff();
            matrix.popPose();
        }

        matrix.popPose();
    }

    protected double getYOffset() {
        return 1.5D;
    }
    /*
    protected void translateItem(boolean isBlock, float position, float rotation, float offset1, float offset2) {
        GlStateManager.translate(0.5F, 0.05F, 0.5F);
        GlStateManager.rotate(-15, 0F, 1F, 0F);
        GlStateManager.scale(0.25F, 0.25F, 0.25F);
        if (!isBlock) {
            GlStateManager.rotate(rotation, 0F, 1F, 0F);
            GlStateManager.rotate(-190, 0F, 1F, 0F);
            GlStateManager.translate(offset1 * 3F, offset2 * 3.5F, position * 0.75F);
        } else {
            GlStateManager.rotate(90, 1F, 0F, 0F);
            GlStateManager.translate(offset1 * 1.4F, 0.8F - offset2 * 2.5F, position - 1F);
        }
    }

    protected void renderItem(ItemStack stack, SpecialRenderData render, int i) {
        renderItem(stack, () -> translateItem(stack.getItem() instanceof ItemBlock, render.heightOffset[i], render.rotations[i], render.offset1[i], render.offset2[i]));
    }

    protected void renderItem(@Nonnull ItemStack stack, Runnable... r) {
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (Minecraft.isAmbientOcclusionEnabled())  {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        } else  {
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        for (Runnable r2: r) {
            r2.run(); //Execute the translations
        }

        GlStateManager.blendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
        GL14.glBlendColor(1F, 1F, 1F, 1F);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, FIXED);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    protected void renderGuiTexture(Direction facing, TextureAtlasSprite sprite, float x, float y, float z, float size, int texUMin, int texVMin, int uWidth, int vHeight) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vb = tessellator.getBuffer();
        if (sprite != null) {
            bindTexture(LOCATION_BLOCKS_TEXTURE);
            double uMin = (double) sprite.getMinU();
            double uMax = (double) sprite.getMaxU();
            double vMin = (double) sprite.getMinV();
            double vMax = (double) sprite.getMaxV();

            if (facing == EnumFacing.NORTH) {
                GlStateManager.rotate(90F, 1F, 0F, 0F);
                GlStateManager.rotate(180F, 0F, 1F, 0F);
                vb.begin(7, POSITION_TEX);
                vb.pos(size / 2f, -size / 2f, -size / 2f).tex(uMax, vMax).endVertex();//Top Right
                vb.pos(size / 2f, -size / 2f, size / 2f).tex(uMax, vMin).endVertex(); //Top Left
                vb.pos(-size / 2f, -size / 2f, size / 2f).tex(uMin, vMin).endVertex(); //Bottom Left
                vb.pos(-size / 2f, -size / 2f, -size / 2f).tex(uMin, vMax).endVertex(); //Bottom Right
                tessellator.draw();
            } else if (facing == EnumFacing.SOUTH) {
                GlStateManager.rotate(90F, 1F, 0F, 0F);
                vb.begin(7, POSITION_TEX);
                vb.pos(size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex();
                vb.pos(size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex();
                vb.pos(-size / 2f, 0, -size / 2f).tex(uMin, vMin).endVertex();
                vb.pos(-size / 2f, 0, size / 2f).tex(uMin, vMax).endVertex();
                tessellator.draw();
            } else if (facing == EnumFacing.WEST) {
                GlStateManager.rotate(90F, 1F, 0F, 0F);
                GlStateManager.rotate(90F, 0F, 0F, 1F);
                vb.begin(7, POSITION_TEX);
                vb.pos(size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex();
                vb.pos(size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex();
                vb.pos(-size / 2f, 0, -size / 2f).tex(uMin, vMin).endVertex();
                vb.pos(-size / 2f, 0, size / 2f).tex(uMin, vMax).endVertex();
                tessellator.draw();
            } else {
                GlStateManager.rotate(180F, 0F, 0F, 1F);
                GlStateManager.rotate(90F, 1F, 0F, 0F);
                GlStateManager.rotate(270F, 0F, 0F, 1F);
                vb.begin(7, POSITION_TEX);
                vb.pos(size / 2f, -size / 2f, -size / 2f).tex(uMax, vMax).endVertex();//Top Right
                vb.pos(size / 2f, -size / 2f, size / 2f).tex(uMax, vMin).endVertex(); //Top Left
                vb.pos(-size / 2f, -size / 2f, size / 2f).tex(uMin, vMin).endVertex(); //Bottom Left
                vb.pos(-size / 2f, -size / 2f, -size / 2f).tex(uMin, vMax).endVertex(); //Bottom
                tessellator.draw();
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    protected void renderFluidCube(ResourceLocation fluid, float x, float y, float z, float size) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vb = tessellator.getBuffer();
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.toString());
        if (sprite != null) {
            bindTexture(LOCATION_BLOCKS_TEXTURE);
            double uMin = (double) sprite.getMinU();
            double uMax = (double) sprite.getMaxU();
            double vMin = (double) sprite.getMinV();
            double vMax = (double) sprite.getMaxV();

            //Draw Top
            //
            vb.begin(7, POSITION_TEX);
            vb.pos(size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex();
            vb.pos(size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex();
            vb.pos(-size / 2f, 0, -size / 2f).tex(uMin, vMin).endVertex();
            vb.pos(-size / 2f, 0, size / 2f).tex(uMin, vMax).endVertex();
            tessellator.draw();

            //Draw Bottom
            vb.begin(7, POSITION_TEX);
            vb.pos(size / 2f, -size / 2f, -size / 2f).tex(uMax, vMax).endVertex();//Top Right
            vb.pos(size / 2f, -size / 2f, size / 2f).tex(uMax, vMin).endVertex(); //Top Left
            vb.pos(-size / 2f, -size / 2f, size / 2f).tex(uMin, vMin).endVertex(); //Bottom Left
            vb.pos(-size / 2f, -size / 2f, -size / 2f).tex(uMin, vMax).endVertex(); //Bottom Right
            tessellator.draw();

            //Draw Side 1
            vb.begin(7, POSITION_TEX);
            vb.pos(-size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex();
            vb.pos(-size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex();
            vb.pos(-size / 2f, -size / 2f, -size / 2f).tex(uMin, vMin).endVertex();
            vb.pos(-size / 2f, -size / 2f, size / 2f).tex(uMin, vMax).endVertex();
            tessellator.draw();

            //Draw Side 2
            vb.begin(7, POSITION_TEX);
            vb.pos(size / 2f, 0, -size / 2f).tex(uMax, vMax).endVertex();
            vb.pos(size / 2f, 0, size / 2f).tex(uMax, vMin).endVertex();
            vb.pos(size / 2f, -size / 2f, size / 2f).tex(uMin, vMin).endVertex();
            vb.pos(size / 2f, -size / 2f, -size / 2f).tex(uMin, vMax).endVertex();
            tessellator.draw();

            //Draw Side 3
            vb.begin(7, POSITION_TEX);
            vb.pos(size / 2f, 0, size / 2f).tex(uMax, vMax).endVertex(); // Top Right
            vb.pos(-size / 2f, 0, size / 2f).tex(uMax, vMin).endVertex(); //Top Left
            vb.pos(-size / 2f, -size / 2f, size / 2f).tex(uMin, vMin).endVertex(); //Bottom Left
            vb.pos(size / 2f, -size / 2f, size / 2f).tex(uMin, vMax).endVertex(); //Bottom Right
            tessellator.draw();

            //Draw Side 2
            vb.begin(7, POSITION_TEX);
            vb.pos(-size / 2f, 0, -size / 2f).tex(uMax, vMax).endVertex(); //Top Right
            vb.pos(size / 2f, 0, -size / 2f).tex(uMax, vMin).endVertex(); //Top Left
            vb.pos(size / 2f, -size / 2f, -size / 2f).tex(uMin, vMin).endVertex(); //Bottom Left
            vb.pos(-size / 2f, -size / 2f, -size / 2f).tex(uMin, vMax).endVertex(); //Bottom Right
            tessellator.draw();
        }

        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    @SuppressWarnings("ConstantConditions")
    protected void renderFluidPlane(ResourceLocation fluid, float x, float y, float z, float width, float length) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vb = tessellator.getBuffer();
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.toString());
        if (sprite != null) {
            bindTexture(LOCATION_BLOCKS_TEXTURE);
            double uMin = (double) sprite.getMinU();
            double uMax = (double) sprite.getMaxU();
            double vMin = (double) sprite.getMinV();
            double vMax = (double) sprite.getMaxV();

            vb.begin(7, POSITION_TEX);
            vb.pos(width / 2f, 0, length / 2f).tex(uMax, vMax).endVertex();
            vb.pos(width / 2f, 0, -length / 2f).tex(uMax, vMin).endVertex();
            vb.pos(-width / 2f, 0, -length / 2f).tex(uMin, vMin).endVertex();
            vb.pos(-width / 2f, 0, length / 2f).tex(uMin, vMax).endVertex();
            tessellator.draw();
        }

        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    } */
}
