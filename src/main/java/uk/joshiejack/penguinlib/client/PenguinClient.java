package uk.joshiejack.penguinlib.client;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.PenguinLib;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = PenguinLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PenguinClient extends RenderState {
    public static final ResourceLocation SPEECH_BUBBLE = new ResourceLocation(PenguinLib.MODID, "extra/speech_bubble");

    public PenguinClient(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
        super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
    }

    @SubscribeEvent
    public static void loadModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(SPEECH_BUBBLE);
    }

}