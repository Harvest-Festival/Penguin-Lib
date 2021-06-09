package uk.joshiejack.penguinlib.data.custom;

import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.IOUtils;
import uk.joshiejack.penguinlib.PenguinLib;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CustomLoader {
    private static final int PATH_SUFFIX_LENGTH = ".json".length();

    @SuppressWarnings("unchecked")
    private static void register(CustomObject co) {
        CustomObject.TYPE_REGISTRY.get(co.type).register(co.name, co.data);
    }

    @SuppressWarnings("unchecked")
    public static <I extends CustomObject> List<I> loadJson(IResourceManager rm, String dir) {
        List<I> list = new ArrayList<>();
        for (ResourceLocation rl : rm.listResources(dir, (fileName) -> fileName.endsWith(".json"))) {
            try {
                IResource resource = rm.getResource(rl);
                InputStream is = resource.getInputStream();
                Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                I i = (I) PenguinGson.get().fromJson(IOUtils.toString(reader), CustomObject.class);
                if (i != null)
                    list.add(i);
                else PenguinLib.LOGGER.error("FAILED TO LOAD " + rl);
            } catch (IllegalArgumentException | IOException ex) {
                PenguinLib.LOGGER.error("Couldn't kill data file from {}", rl, ex);
            }
        }

        return list;
    }
}
