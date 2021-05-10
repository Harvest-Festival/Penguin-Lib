package uk.joshiejack.penguinlib.potion;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectType;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractExpiringEffect extends PenguinEffect {
    public AbstractExpiringEffect(EffectType type, int color) {
        super(type, color);
    }

    boolean expiring = false;

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        expiring = duration == 1;
        return expiring || super.isDurationEffectTick(duration, amplifier);
    }

    @Nonnull
    @Override
    public List<ItemStack> getCurativeItems() {
        return Lists.newArrayList();
    }
}
