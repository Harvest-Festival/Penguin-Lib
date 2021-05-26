package uk.joshiejack.penguinlib.data.generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.item.PenguinItems;

public class PenguinLanguage extends LanguageProvider {
    public PenguinLanguage(DataGenerator gen) {
        super(gen, PenguinLib.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(PenguinItems.DEEP_BOWL, "Deep Bowl");
        addItem(PenguinItems.GLASS, "Glass");
        addItem(PenguinItems.JAM_JAR, "Jam Jar");
        addItem(PenguinItems.PICKLING_JAR, "Pickling Jar");
        addItem(PenguinItems.MUG, "Mug");
        addItem(PenguinItems.PLATE, "Plate");
        addItem(PenguinItems.UNFIRED_MUG, "Unfired Mug");
        addItem(PenguinItems.UNFIRED_PLATE, "Unfired Plate");
        addItem(PenguinItems.PENGUIN_BANNER_PATTERN, "Banner Pattern");
        add("item.penguinlib.penguin_banner_pattern.desc", "Penguin");
        add("button.penguinlib.next", "Next");
        add("button.penguinlib.previous", "Previous");
    }
}
