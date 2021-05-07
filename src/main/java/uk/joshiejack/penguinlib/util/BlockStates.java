package uk.joshiejack.penguinlib.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;

public class BlockStates {
    public static final BlockState WET_SOIL = Blocks.FARMLAND.defaultBlockState().setValue(FarmlandBlock.MOISTURE, 7);
    public static final BlockState DRY_SOIL = Blocks.FARMLAND.defaultBlockState().setValue(FarmlandBlock.MOISTURE, 0);
    public static final BlockState TALL_GRASS = Blocks.TALL_GRASS.defaultBlockState();
    public static final BlockState BEDROCK = Blocks.BEDROCK.defaultBlockState();
    public static final BlockState DIRT = Blocks.DIRT.defaultBlockState();
    public static final BlockState AIR = Blocks.AIR.defaultBlockState();
}
