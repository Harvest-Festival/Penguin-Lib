package uk.joshiejack.penguinlib.potion;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class IncurableEffect extends PenguinEffect {
    private static final List<ItemStack> EMPTY = new ArrayList<>();

    public IncurableEffect(EffectType type, int color) {
        super(type, color);
    }

    @Nonnull
    @Override
    public List<ItemStack> getCurativeItems() {
        return EMPTY;
    }
}
