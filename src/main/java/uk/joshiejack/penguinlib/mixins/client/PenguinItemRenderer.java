package uk.joshiejack.penguinlib.mixins.client;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.VertexBuilderUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.joshiejack.penguinlib.client.renderer.ShadowRenderer;

/* Works like the glint texture but makes items have a shadow texture */
@Mixin(ItemRenderer.class)
public class PenguinItemRenderer {
    @Inject(method = "getFoilBuffer", at = @At("HEAD"), cancellable = true)
    private static void getShadowBuffer(IRenderTypeBuffer buffer, RenderType type, boolean isItem, boolean isShiny, CallbackInfoReturnable<IVertexBuilder> cir) {
        if (ShadowRenderer.isEnabled())
            cir.setReturnValue(VertexBuilderUtils.create(buffer.getBuffer(ShadowRenderer.shadow()), buffer.getBuffer(type)));
    }

    @Inject(method = "getFoilBufferDirect", at = @At("HEAD"), cancellable = true)
    private static void getShadowBufferDirect(IRenderTypeBuffer buffer, RenderType type, boolean isItem, boolean isShiny, CallbackInfoReturnable<IVertexBuilder> cir) {
        if (ShadowRenderer.isEnabled())
            cir.setReturnValue(VertexBuilderUtils.create(buffer.getBuffer(ShadowRenderer.shadow()), buffer.getBuffer(type)));
    }
}