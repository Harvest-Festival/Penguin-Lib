package uk.joshiejack.penguinlib.item.base;

import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;

public class PenguinFishingRodItem extends FishingRodItem {
    private final int quality;

    public PenguinFishingRodItem(Item.Properties properties, int quality) {
        super(properties);
        this.quality = quality;
    }

    @Override
    public int getEnchantmentValue() {
        return quality;
    }
}
