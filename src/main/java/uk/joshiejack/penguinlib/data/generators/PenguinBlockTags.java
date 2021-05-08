package uk.joshiejack.penguinlib.data.generators;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import uk.joshiejack.penguinlib.PenguinLib;

import javax.annotation.Nullable;

public final class PenguinBlockTags extends BlockTagsProvider {
    public PenguinBlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, PenguinLib.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {}
}
