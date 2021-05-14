package uk.joshiejack.penguinlib.data;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID)
public class VillagerTrading {
    private static final Map<VillagerProfession, Int2ObjectMap<MerchantOffer>> TRADES = new HashMap<>();

    @SubscribeEvent
    public static void onReload(VillagerTradesEvent event) {
        if (TRADES.containsKey(event.getType()))
            TRADES.get(event.getType()).forEach((tier, offer) -> event.getTrades().get(tier).add(new Trade(offer)));
    }

    static class Trade implements VillagerTrades.ITrade {
        private final MerchantOffer offer;

        public Trade(MerchantOffer offer) {
            this.offer = offer;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(@Nonnull Entity player, @Nonnull Random rando) {
            return new MerchantOffer(offer.getBaseCostA(), offer.getResult(), offer.getMaxUses(), offer.getXp(), offer.getPriceMultiplier());
        }
    }

    private static Int2ObjectMap<MerchantOffer> get(VillagerProfession profession) {
        if (!TRADES.containsKey(profession))
            TRADES.put(profession, new Int2ObjectOpenHashMap<>());
        return TRADES.get(profession);
    }

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent event) {
        TRADES.clear();
        event.table("villager_trades").rows().forEach(row -> {
            VillagerProfession profession = ForgeRegistries.PROFESSIONS.getValue(row.getRL("profession"));
            Item input = row.item("input item");
            Item output = row.item("output item");
            if (profession != null && input != null && output != null) {
                get(profession).put(row.getAsInt("tier"),
                        new MerchantOffer(
                                new ItemStack(input, row.getAsInt("input amount")),
                                new ItemStack(output, row.getAsInt("output amount")),
                                row.getAsInt("max trades"),
                                row.getAsInt("experience"),
                                row.getAsFloat("price multiplier")
                        ));
            }
        });
    }
}
