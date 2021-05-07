package uk.joshiejack.penguinlib.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;

@Cancelable
@Event.HasResult
public class UseWateringCanEvent extends PlayerEvent {
    private final ItemStack current;
    private final World world;
    private final BlockPos pos;

    public UseWateringCanEvent(PlayerEntity player, @Nonnull ItemStack current, World world, BlockPos pos) {
        super(player);
        this.current = current;
        this.world = world;
        this.pos = pos;
    }

    @Nonnull
    public ItemStack getCurrent()
    {
        return current;
    }

    public World getWorld()
    {
        return world;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public static class Post extends UseWateringCanEvent {
        public Post(PlayerEntity player, @Nonnull ItemStack current, World world, BlockPos pos) {
            super(player, current, world, pos);
        }
    }
}
