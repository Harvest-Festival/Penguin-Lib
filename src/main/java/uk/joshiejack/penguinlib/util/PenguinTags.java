package uk.joshiejack.penguinlib.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.*;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.PenguinLib;

import javax.annotation.Nonnull;

public class PenguinTags {
    public static final ITag.INamedTag<Item> BREAD = forgeItemTag("bread");
    public static final ITag.INamedTag<Item> RAW_FISHES = forgeItemTag("raw_fishes");
    public static final ITag.INamedTag<Item> CROPS_APPLE = forgeItemTag("crops/apple");
    public static final ITag.INamedTag<Item> CROPS_PUMPKIN = forgeItemTag("crops/pumpkin");
    public static final ITag.INamedTag<Item> CROPS_MELON = forgeItemTag("crops/melon");
    public static final ITag.INamedTag<Item> FUNGI = forgeItemTag("fungi");
    //######################################### TOOLS ###########################################
    public static final ITag.INamedTag<Item> TOOLS = forgeItemTag("tools");
    public static final ITag.INamedTag<Item> PICKAXES = forgeItemTag("tools/pickaxes");
    public static final ITag.INamedTag<Item> AXES = forgeItemTag("tools/axes");
    public static final ITag.INamedTag<Item> SWORDS = forgeItemTag("tools/swords");
    public static final ITag.INamedTag<Item> HAMMERS = forgeItemTag("tools/hammers");
    public static final ITag.INamedTag<Item> SHOVELS = forgeItemTag("tools/shovels");
    public static final ITag.INamedTag<Item> HOES = forgeItemTag("tools/hoes");
    public static final ITag.INamedTag<Item> SICKLES = forgeItemTag("tools/sickles");
    public static final ITag.INamedTag<Item> FISHING_RODS = forgeItemTag("tools/fishing_rods");
    public static final ITag.INamedTag<Item> WATERING_CANS = forgeItemTag("tools/watering_cans");
    //######################################### Hammer AOE ######################################
    public static final ITag.INamedTag<Block> SMASHABLE = penguinBlockTag("smashable");
    public static final ITag.INamedTag<Item> CLOCKS = penguinItemTag("clocks");

    @Deprecated //TODO: Remove in 0.6
    public static ITag.INamedTag<Block> penguinTag(@Nonnull String name) {
        return BlockTags.createOptional(new ResourceLocation(PenguinLib.MODID,  name));
    }

    public static ITag.INamedTag<Block> penguinBlockTag(@Nonnull String name) {
        return BlockTags.createOptional(new ResourceLocation(PenguinLib.MODID,  name));
    }

    public static ITag.INamedTag<Item> penguinItemTag(@Nonnull String name) {
        return ItemTags.createOptional(new ResourceLocation(PenguinLib.MODID,  name));
    }

    public static ITag.INamedTag<Block> forgeBlockTag(@Nonnull String name) {
        return BlockTags.createOptional(new ResourceLocation("forge",  name));
    }

    public static ITag.INamedTag<Item> forgeItemTag(@Nonnull String name) {
        return ItemTags.createOptional(new ResourceLocation("forge",  name));
    }

    public static ITag.INamedTag<EntityType<?>> forgeEntityTag(@Nonnull String name) {
        return EntityTypeTags.createOptional(new ResourceLocation("forge",  name));
    }

    public static ITag.INamedTag<Fluid> forgeFluidTag(@Nonnull String name) {
        return FluidTags.createOptional(new ResourceLocation("forge",  name));
    }

    @Deprecated
    public static ITag.INamedTag<Item> forgeTag(@Nonnull String name) {
        return ItemTags.createOptional(new ResourceLocation("forge",  name));
    }

}
