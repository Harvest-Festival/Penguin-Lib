package uk.joshiejack.penguinlib.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.PenguinLib;

import javax.annotation.Nonnull;

public class PenguinTags {
    public static final ITag.INamedTag<Item> BREAD = forgeTag("bread");
    public static final ITag.INamedTag<Item> RAW_FISHES = forgeTag("raw_fishes");
    public static final ITag.INamedTag<Item> APPLE = PenguinTags.forgeTag("crops/apple");
    public static final ITag.INamedTag<Block> SMASHABLE = penguinTag("smashable");

    public static ITag.INamedTag<Block> penguinTag(@Nonnull String name) {
        return BlockTags.createOptional(new ResourceLocation(PenguinLib.MODID,  name));
    }

    public static ITag.INamedTag<Item> forgeTag(@Nonnull String name) {
        return ItemTags.createOptional(new ResourceLocation("forge",  name));
    }

}
