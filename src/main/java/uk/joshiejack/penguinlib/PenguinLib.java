package uk.joshiejack.penguinlib;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
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
import uk.joshiejack.penguinlib.data.PenguinLibDatabase;
import uk.joshiejack.penguinlib.data.custom.CustomObject;
import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.interfaces.IModPlugin;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.ReflectionHelper;

import java.io.File;
import java.util.*;
import java.util.function.BiConsumer;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(PenguinLib.MODID)
public class PenguinLib {
    public static final String MODID = "penguinlib";
    public static final IEventBus EVENT_BUS = BusBuilder.builder().build();
    public static final Logger LOGGER = LogManager.getLogger();
    private static final Type LOADER = Type.getType(PenguinLoader.class);
    private static final Type PACKET = Type.getType(PenguinLoader.Packet.class);
    private final List<Pair<String, Class<? extends IModPlugin>>> PLUGINS = new ArrayList<>();

    //public static final boolean IS_DEOBF = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    private static File directory;

    public PenguinLib() {
        PenguinLib.EVENT_BUS.register(this);
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::registerRegistries);
        eventBus.addListener(this::loadPlugins);
        registerPenguinLoaderData(eventBus);
        directory = new File("config", MODID);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerRegistries(CollectRegistryEvent.Loader event) {
        event.add(CustomObject.Data.class, (data, string) -> CustomObject.TYPE_REGISTRY.put(string, Objects.requireNonNull(ReflectionHelper.newInstance(data))));
        event.add(IModPlugin.class, (data, string) -> PLUGINS.add(Pair.of(string, data)));
    }

    @SuppressWarnings("unchecked")
    private void registerPenguinLoaderData(IEventBus eventBus) {
        Map<Class<?>, BiConsumer<Class<?>, String>> processors = new HashMap<>();
        eventBus.post(new CollectRegistryEvent.Loader(processors));
        ModList.get().getAllScanData().stream()
                .map(ModFileScanData::getAnnotations)
                .flatMap(Collection::stream) //Either of the penguin annotation or the packet annotation is ok
                .filter(a -> LOADER.equals(a.getAnnotationType()) || PACKET.equals(a.getAnnotationType()))//, i trust that i will use the packet one only on packets ;)
                .forEach((a -> {
                    try {
                        Class<?> clazz = Class.forName(a.getClassType().getClassName());
                        if (PenguinPacket.class.isAssignableFrom(clazz))
                            PenguinNetwork.registerPacket((Class<PenguinPacket>) clazz,
                                    NetworkDirection.valueOf(((ModAnnotation.EnumHolder)a.getAnnotationData().get("value")).getValue()));
                        else {
                            processors.entrySet().stream()
                                    .filter((entry) -> entry.getKey().isAssignableFrom(clazz))
                                    .forEach(entry -> entry.getValue().accept(clazz, (String) a.getAnnotationData().get("value")));
                        }
                    } catch (ClassNotFoundException ignored) {}
                }));
    }

    private void loadPlugins(FMLCommonSetupEvent event) {
        PLUGINS.stream().filter(pair -> ModList.get().isLoaded(pair.getKey()))
                .forEach(pair -> Objects.requireNonNull(ReflectionHelper.newInstance(pair.getValue())).setup());
        PLUGINS.clear(); //Kill them off
    }

    @SubscribeEvent
    public static void onDataGathering(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        if (event.includeServer())
            generator.addProvider(new PenguinLibDatabase(generator));
    }

    //NetworkRegistry.INSTANCE.registerGuiHandler(instance, this);
    //LootFunctionManager.registerFunction(new SetEnum.Serializer());
    //LootFunctionManager.registerFunction(new SetString.Serializer());
    //LootFunctionManager.registerFunction(new ApplyFortune.Serializer());
    //LootConditionManager.registerCondition(new ConditionObtained.Serializer());
    //LootConditionManager.registerCondition(new ConditionUnobtained.Serializer());
    //CustomLoader.loadObjects(); //Load the shit earlier
    //CustomLoader.startLoading();

    /*@SubscribeEvent
    public static void onCollectForRegistration(CollectRegistryEvent event) {
        event.add(AbstractCustomData.class, (d, c, s, l) -> AbstractCustomObject.TYPE_REGISTRY.put(l, c.newInstance()));
        event.add(Placeable.class, (d, c, s, l) -> PlaceableHelper.TYPE_REGISTRY.put(l, d.getClassName()));
        event.add(PenguinPacket.class, ((d, clazz, sides, loadingData) -> sides.forEach((side) -> PenguinNetwork.registerPacket(clazz, side))));
        event.add(Holder.class, ((d, c, s, l) -> c.newInstance().register()));
        event.add(TileEntity.class, ((d, c, s, l) -> GameRegistry.registerTileEntity(c, new ResourceLocation(MODID, l))));
        event.add(CommandTreeBase.class, ((d, c, s, l) -> PenguinLib.TREES.add((CommandTreeBase) c.getField("INSTANCE").get(null))));
        event.add(DailyTicker.class, ((d, c, s, l) -> TickerRegistry.registerType(l, c.newInstance())));
        event.add(ContainerBook.class, (((d, c, s, l) -> ContainerBook.REGISTRY.put(Byte.parseByte(l), c))));
    } */

    /*
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        //Load the custom tabs with their actual values
        CustomLoader.tabs.forEach(CustomPenguinTab::init); //Load the tabs after everything
        if (PenguinConfig.addDishRecipes) {
            RecipeHelper helper = new RecipeHelper(event.getRegistry(), MODID);
            if (addPlate) GameRegistry.addSmelting(PenguinItems.DINNERWARE.getStackFromEnum(PLATE_UNFIRED), PenguinItems.DINNERWARE.getStackFromEnum(PLATE_FIRED), 0.2F);
            if (addGlass) helper.shapedRecipe("glass", PenguinItems.DINNERWARE.getStackFromEnum(GLASS, 4), "G G", " G ", 'G', "paneGlassColorless");
            if (addPicklingJar) helper.shapedRecipe("pickling_jar", PenguinItems.DINNERWARE.getStackFromEnum(PICKLING_JAR, 4), "S ", "G ", "G ", 'G', "blockGlassColorless", 'S', "slabWood");
            if (addPlate) helper.shapedRecipe("plate", PenguinItems.DINNERWARE.getStackFromEnum(PLATE_UNFIRED, 2), "CC", 'C', Items.CLAY_BALL);
            if (addBowl) helper.shapedRecipe("bowl", PenguinItems.DINNERWARE.getStackFromEnum(BOWL, 6), "P P", "SSS", 'P', "plankWood", 'S', "slabWood");
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (!event.world.isClientSide && event.phase == TickEvent.Phase.END && event.world.getWorldTime() % TimeHelper.TICKS_PER_DAY == 1) {
            MinecraftForge.EVENT_BUS.post(new NewDayEvent((ServerWorld) event.world)); //Post the new day event, to update
        }
    }

    private static final List<CommandTreeBase> TREES = Lists.newArrayList();

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        TREES.forEach(event::registerServerCommand);
    } */

    public static File getDirectory() {
        if (directory != null && !directory.exists()) {
            directory.mkdir();
        }

        return directory;
    }

    public static File getCustomFolder() {
        File folder = new File(getDirectory(), "custom");
        if (!folder.exists()) folder.mkdir();
        return folder;
    }

    /*@Nullable
    @Override
    public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
        if (ID == 1) {
            return new ContainerGreenScreen(player.inventory);
        }

        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
        if (ID == 1) return new GuiGreenScreen(new ContainerGreenScreen(player.inventory));
        else {
            Hand hand = x == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
            ItemStack held = player.getItemInHand(hand);
            if (held.getItem() instanceof ItemCustomGuide) {
                return GuiUniversalGuide.getGui((ItemCustomGuide) held.getItem());
            }
        }

        return null;
    }*/
}
