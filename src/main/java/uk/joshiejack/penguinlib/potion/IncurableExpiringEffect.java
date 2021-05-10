package uk.joshiejack.penguinlib.potion;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class IncurableExpiringEffect extends AbstractExpiringEffect {
    private static final List<ItemStack> EMPTY = new ArrayList<>();

    public IncurableExpiringEffect(EffectType type, int color) {
        super(type, color);
    }

    @Nonnull
    @Override
    public List<ItemStack> getCurativeItems() {
        return EMPTY;
    }
}
