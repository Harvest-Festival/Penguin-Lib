package uk.joshiejack.penguinlib.data;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID)
public class Composting {
    private static final Set<Item> ADDED = new HashSet<>();

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent event) {
        ADDED.forEach(ComposterBlock.COMPOSTABLES::removeFloat);
        ADDED.clear();
        event.table("composter").rows().forEach(row -> {
            Item input = row.item();
            if (input != Items.AIR) {
                ADDED.add(input);
                ComposterBlock.COMPOSTABLES.put(input, row.getAsFloat("compost amount"));
            }
        });
    }
}