package uk.joshiejack.penguinlib.client.renderer;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.PenguinLib;

public class ShadowRenderer extends RenderState {
    private static final ResourceLocation SHADOW_TEXTURE_LOCATION = new ResourceLocation(PenguinLib.MODID, "textures/misc/shadow.png");
    private static final RenderType SHADOW = RenderType.create("shadow", DefaultVertexFormats.POSITION_TEX, 7, 256,
            RenderType.State.builder().setTextureState(new TextureState(SHADOW_TEXTURE_LOCATION, true, false))
                    .setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST)
                    .createCompositeState(false));
    private static boolean enabled;

    public ShadowRenderer(String name, Runnable r1, Runnable r2) {
        super(name, r1, r2);
    }

    public static RenderType shadow() {
        return SHADOW;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void enable() {
        enabled = true;
    }

    public static void disable() {
        enabled = false;
    }
}
