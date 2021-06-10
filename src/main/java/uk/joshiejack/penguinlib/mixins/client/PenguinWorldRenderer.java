package uk.joshiejack.penguinlib.mixins.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.vector.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.joshiejack.penguinlib.client.renderer.ShadowRenderer;

@Mixin(WorldRenderer.class)
public abstract class PenguinWorldRenderer {
    @Mutable
    @Final
    @Shadow
    private final RenderTypeBuffers renderBuffers;

    protected PenguinWorldRenderer(RenderTypeBuffers renderBuffers) {
        this.renderBuffers = renderBuffers;
    }


    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/IRenderTypeBuffer$Impl;endBatch(Lnet/minecraft/client/renderer/RenderType;)V"))
    public void render(MatrixStack stack, float f, long l, boolean b, ActiveRenderInfo info, GameRenderer renderer, LightTexture texture, Matrix4f matrix, CallbackInfo ci) {
        IRenderTypeBuffer.Impl irendertypebuffer$impl = this.renderBuffers.bufferSource();
        irendertypebuffer$impl.endBatch(ShadowRenderer.shadow());
    }
}