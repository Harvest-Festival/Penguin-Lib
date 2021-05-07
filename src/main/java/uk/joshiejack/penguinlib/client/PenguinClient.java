package uk.joshiejack.penguinlib.client;

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
public class PenguinClient {
    public static final ResourceLocation SPEECH_BUBBLE = new ResourceLocation(PenguinLib.MODID, "extra/speech_bubble");
    @SubscribeEvent
    public static void loadModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(SPEECH_BUBBLE);
    }
}
