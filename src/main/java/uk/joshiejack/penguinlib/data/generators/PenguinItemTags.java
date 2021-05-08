package uk.joshiejack.penguinlib.data.generators;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
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
    }
}
