package uk.joshiejack.penguinlib.mixins.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeBuffers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.joshiejack.penguinlib.client.renderer.ShadowRenderer;

import java.util.SortedMap;

@Mixin(RenderTypeBuffers.class)
public abstract class PenguinRenderTypeBuffers {
    @Mutable
    @Final
    @Shadow
    private final SortedMap<RenderType, BufferBuilder> fixedBuffers;

    protected PenguinRenderTypeBuffers(SortedMap<RenderType, BufferBuilder> fixedBuffers) {
        this.fixedBuffers = fixedBuffers;
    }

    @Inject(method = "<init>()V", at = @At("TAIL"))
    public void construct(CallbackInfo ci) {
        put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>) fixedBuffers, ShadowRenderer.shadow());
    }

    @Shadow
    private static void put(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> map, RenderType type) {}
}