package uk.joshiejack.penguinlib;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import uk.joshiejack.penguinlib.client.PenguinClientConfig;
import uk.joshiejack.penguinlib.data.LootTableMerger;
import uk.joshiejack.penguinlib.data.custom.CustomObject;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.data.generators.*;
import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.item.PenguinItems;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.ReflectionHelper;
import uk.joshiejack.penguinlib.util.interfaces.IModPlugin;

import java.util.*;
import java.util.function.BiConsumer;


@SuppressWarnings("rawtypes")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(PenguinLib.MODID)
public class PenguinLib {
    public static final String MODID = "penguinlib";
    public static final IEventBus EVENT_BUS = BusBuilder.builder().build();
    public static final Logger LOGGER = LogManager.getLogger();
    private static final Type LOADER = Type.getType(PenguinLoader.class);
    private static final Type PACKET = Type.getType(PenguinLoader.Packet.class);

    public PenguinLib() {
        PenguinLib.EVENT_BUS.register(this);
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        //Custom stuff needs to happen, before the registry loads
        Map<Class<?>, BiConsumer<Class<?>, String>> processors = new HashMap<>();
        processors.put(CustomObject.Data.class,(Class<?> data, String string) ->
                CustomObject.TYPE_REGISTRY.put(string, (CustomObject.Data) Objects.requireNonNull(ReflectionHelper.newInstance(data))));
        registerPenguinLoaderData(processors);
        MinecraftForge.EVENT_BUS.register(this);
        PenguinItems.ITEMS.register(eventBus);
        LootTableMerger.LOOT_MODIFIER_SERIALIZERS.register(eventBus);
        Database.REGISTRY.register(eventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PenguinClientConfig.create());
    }

    @SuppressWarnings("unchecked")
    private void setup(FMLCommonSetupEvent event) {
        List<Pair<String, Class<? extends IModPlugin>>> plugins = new ArrayList<>();
        Map<Class<?>, BiConsumer<Class<?>, String>> processors = new HashMap<>();
        processors.put(IModPlugin.class, (Class<?> data, String string) -> plugins.add(Pair.of(string, (Class<IModPlugin>) data)));
        FMLJavaModLoadingContext.get().getModEventBus().post(new CollectRegistryEvent.Loader(processors));
        //Grab any other things that need to be automagically registered ^
        registerPenguinLoaderData(processors); //Process them and load them
        plugins.stream()
                .filter(pair -> ModList.get().isLoaded(pair.getKey()))
                .forEach(pair -> Objects.requireNonNull(ReflectionHelper.newInstance(pair.getValue())).setup());
        plugins.clear(); //Kill them off
    }

    @SubscribeEvent
    public static void onDataGathering(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new PenguinDatabase(generator));
            BlockTagsProvider blockTags = new PenguinBlockTags(generator, event.getExistingFileHelper());
            generator.addProvider(blockTags);
            generator.addProvider(new PenguinItemTags(generator, blockTags, event.getExistingFileHelper()));
            generator.addProvider(new PenguinRecipes(generator));
        }

        if (event.includeClient()) {
            generator.addProvider(new PenguinLanguage(generator));
            generator.addProvider(new PenguinItemModels(generator, event.getExistingFileHelper()));
        }
    }

    @SuppressWarnings("unchecked")
    private void registerPenguinLoaderData(Map<Class<?>, BiConsumer<Class<?>, String>> processors) {
         ModList.get().getAllScanData().stream()
                .map(ModFileScanData::getAnnotations)
                .flatMap(Collection::stream) //Either of the penguin annotation or the packet annotation is ok
                .filter(a -> LOADER.equals(a.getAnnotationType()) || PACKET.equals(a.getAnnotationType()))//, i trust that i will use the packet one only on packets ;)
                .forEach((a -> {
                    try {
                        Class<?> clazz = Class.forName(a.getClassType().getClassName());
                        if (PenguinPacket.class.isAssignableFrom(clazz))
                            PenguinNetwork.registerPacket((Class<PenguinPacket>) clazz,
                                    NetworkDirection.valueOf(((ModAnnotation.EnumHolder) a.getAnnotationData().get("value")).getValue()));
                        else {
                            processors.entrySet().stream()
                                    .filter((entry) -> entry.getKey().isAssignableFrom(clazz))
                                    .forEach(entry -> entry.getValue().accept(clazz, (String) a.getAnnotationData().get("value")));
                        }
                    } catch (ClassNotFoundException ignored) {
                    }
                }));
    }
}
