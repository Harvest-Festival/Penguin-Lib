package uk.joshiejack.penguinlib.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nonnull;

public class BookContainer extends Container {
    protected BookContainer(int windowID) {
        super(PenguinContainers.BOOK.get(), windowID);
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity player) {
        return true;
    }
}
