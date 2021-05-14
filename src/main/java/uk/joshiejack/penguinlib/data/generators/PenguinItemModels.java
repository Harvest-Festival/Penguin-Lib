package uk.joshiejack.penguinlib.data.generators;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.item.PenguinItems;

import java.util.Objects;

public class PenguinItemModels extends ItemModelProvider {
    public PenguinItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, PenguinLib.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        PenguinItems.ITEMS.getEntries().stream()
                .map(RegistryObject::get)
                .forEach(item -> {
                    String path = Objects.requireNonNull(item.getRegistryName()).getPath();
                    if (item instanceof BlockItem)
                        getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
                    else
                        singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
                });
    }
}
