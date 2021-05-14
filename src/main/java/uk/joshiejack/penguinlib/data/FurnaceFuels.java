package uk.joshiejack.penguinlib.data;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.Item;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID)
public class FurnaceFuels {
    private static final Object2IntMap<Item> FUELS = new Object2IntOpenHashMap<>();

    @SubscribeEvent
    public static void onFuelBurnCheck(FurnaceFuelBurnTimeEvent event) {
        if (FUELS.containsKey(event.getItemStack().getItem()))
            event.setBurnTime(FUELS.getInt(event.getItemStack().getItem()));
    }

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent event) {
        FUELS.clear();
        event.table("furnace_fuels").rows().forEach(row -> {
            Item input = row.item();
            if (input != null)
                FUELS.put(input, row.getAsInt("burn time"));
        });
    }
}
