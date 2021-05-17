package uk.joshiejack.penguinlib.data.generators;

import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.util.PenguinTags;

import javax.annotation.Nullable;

public final class PenguinItemTags extends ItemTagsProvider {
    public PenguinItemTags(DataGenerator generator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagProvider, PenguinLib.MODID, existingFileHelper);
    }

    @Override
    public void addTags() {
        tag(PenguinTags.BREAD).add(Items.BREAD);
        Builder<Item> rawTag = tag(PenguinTags.RAW_FISHES);
        rawTag.add(Items.COD, Items.SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH);
        tag(PenguinTags.APPLE).add(Items.APPLE);
        tag(Tags.Items.CROPS).add(Items.APPLE);
        tag(PenguinTags.PUMPKIN).add(Blocks.PUMPKIN.asItem());
        tag(PenguinTags.MELON).add(Items.MELON);
        tag(PenguinTags.FUNGUS).add(Items.WARPED_FUNGUS, Items.CRIMSON_FUNGUS);

        tag(PenguinTags.HAMMERS);
        tag(PenguinTags.SCYTHES);
        tag(PenguinTags.SICKLES);
        tag(PenguinTags.WATERING_CANS);
        tag(PenguinTags.SHEARS).add(Items.SHEARS);
        Builder<Item> swords = tag(PenguinTags.SWORDS);
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item instanceof SwordItem)
                .forEach(swords::add);
        Builder<Item> axes = tag(PenguinTags.AXES);
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item.getToolTypes(new ItemStack(item)).contains(ToolType.AXE))
                .forEach(axes::add);
        Builder<Item> pickaxes = tag(PenguinTags.PICKAXES);
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item.getToolTypes(new ItemStack(item)).contains(ToolType.PICKAXE))
                .forEach(pickaxes::add);
        Builder<Item> shovels = tag(PenguinTags.SHOVELS);
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item.getToolTypes(new ItemStack(item)).contains(ToolType.SHOVEL))
                .forEach(shovels::add);
        Builder<Item> hoes = tag(PenguinTags.HOES);
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item.getToolTypes(new ItemStack(item)).contains(ToolType.HOE))
                .forEach(hoes::add);
        Builder<Item> fishing_rods = tag(PenguinTags.FISHING_RODS);
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item instanceof FishingRodItem)
                .forEach(fishing_rods::add);
    }
}
