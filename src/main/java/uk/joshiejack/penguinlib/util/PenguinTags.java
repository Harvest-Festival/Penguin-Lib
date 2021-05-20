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
    public static final ITag.INamedTag<Item> BREADS = forgeTag("breads");
    public static final ITag.INamedTag<Item> RAW_FISHES = forgeTag("raw_fishes");
    public static final ITag.INamedTag<Item> CROPS_APPLE = PenguinTags.forgeTag("crops/apple");
    public static final ITag.INamedTag<Item> CROPS_PUMPKIN = PenguinTags.forgeTag("crops/pumpkin");
    public static final ITag.INamedTag<Item> CROPS_MELON = PenguinTags.forgeTag("crops/melon");
    public static final ITag.INamedTag<Item> FUNGI = forgeTag("fungi");
    //######################################### TOOLS ###########################################
    public static final ITag.INamedTag<Item> TOOLS = forgeTag("tools");
    public static final ITag.INamedTag<Item> PICKAXES = forgeTag("tools/pickaxe");
    public static final ITag.INamedTag<Item> AXES = forgeTag("tools/axe");
    public static final ITag.INamedTag<Item> SWORDS = forgeTag("tools/sword");
    public static final ITag.INamedTag<Item> HAMMERS = forgeTag("tools/hammer");
    public static final ITag.INamedTag<Item> SHOVELS = forgeTag("tools/shovel");
    public static final ITag.INamedTag<Item> HOES = forgeTag("tools/hoe");
    public static final ITag.INamedTag<Item> SICKLES = forgeTag("tools/sickle");
    public static final ITag.INamedTag<Item> SCYTHES = forgeTag("tools/scythe");
    public static final ITag.INamedTag<Item> FISHING_RODS = forgeTag("tools/fishing_rod");
    public static final ITag.INamedTag<Item> WATERING_CANS = forgeTag("tools/watering_can");
    //######################################### Hammer AOE ######################################
    public static final ITag.INamedTag<Block> SMASHABLE = penguinTag("smashable");

    public static ITag.INamedTag<Block> penguinTag(@Nonnull String name) {
        return BlockTags.createOptional(new ResourceLocation(PenguinLib.MODID,  name));
    }

    public static ITag.INamedTag<Item> forgeTag(@Nonnull String name) {
        return ItemTags.createOptional(new ResourceLocation("forge",  name));
    }

}
