package uk.joshiejack.penguinlib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.FontResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import uk.joshiejack.penguinlib.PenguinLib;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = PenguinLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PenguinClient {
    public static final ResourceLocation SPEECH_BUBBLE = new ResourceLocation(PenguinLib.MODID, "extra/speech_bubble");
    public static final ResourceLocation FANCY = new ResourceLocation(PenguinLib.MODID, "fancy");
    public static final Lazy<FontRenderer> FANCY_FONT = Lazy.of(PenguinClient::createFancyFont);

    private static FontRenderer createFancyFont() {
        FontResourceManager manager = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getInstance(), "field_211501_aD");
        Map<ResourceLocation, Font> fontSets = ObfuscationReflectionHelper.getPrivateValue(FontResourceManager.class, manager, "field_238546_d_");
        return new FontRenderer((m) -> {
            assert fontSets != null;
            return fontSets.get(FANCY);
        });
    }

    @SubscribeEvent
    public static void loadModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(SPEECH_BUBBLE);
    }
}