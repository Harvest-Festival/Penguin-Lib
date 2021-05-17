package uk.joshiejack.penguinlib.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nonnull;

public abstract class AbstractBookContainer extends Container {
    public AbstractBookContainer(ContainerType<?> type, int windowID) {
        super(type, windowID);
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity player) {
        return true;
    }
}
