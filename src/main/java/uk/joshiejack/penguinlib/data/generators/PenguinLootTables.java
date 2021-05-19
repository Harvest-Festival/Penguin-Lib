package uk.joshiejack.penguinlib.data.generators;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.item.PenguinItems;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PenguinLootTables extends LootTableProvider {
    public PenguinLootTables(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationTracker validationtracker) {
        map.forEach((name, table) -> LootTableManager.validate(validationtracker, name, table));
    }

    @Nonnull
    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(Pair.of(Chests::new, LootParameterSets.CHEST));
    }

    public static class Chests extends ChestLootTables {
        private static ResourceLocation chest(ResourceLocation rl) {
            return new ResourceLocation(PenguinLib.MODID, rl.getPath());
        }

        @Override
        public void accept(@Nonnull BiConsumer<ResourceLocation, LootTable.Builder> builder) {
            builder.accept(chest(LootTables.IGLOO_CHEST), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                    .add(ItemLootEntry.lootTableItem(PenguinItems.PENGUIN_BANNER_PATTERN.get()).setWeight(1))));
            builder.accept(chest(LootTables.VILLAGE_TAIGA_HOUSE), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                    .add(ItemLootEntry.lootTableItem(PenguinItems.PENGUIN_BANNER_PATTERN.get()).setWeight(1))));
            builder.accept(chest(LootTables.VILLAGE_SNOWY_HOUSE), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                    .add(ItemLootEntry.lootTableItem(PenguinItems.PENGUIN_BANNER_PATTERN.get()).setWeight(1))));
        }
    }
}
