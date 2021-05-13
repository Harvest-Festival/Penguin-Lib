package uk.joshiejack.penguinlib.item.base;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class PenguinItem extends Item {
    private final UseAction useAction;
    private final int useDuration;

    public PenguinItem(Item.Properties properties) {
        super(properties);
        Properties pp = properties instanceof Properties ? ((Properties)properties) : null;
        this.useAction = pp == null ? UseAction.NONE : pp.useAction;
        this.useDuration = pp == null ? 0 : pp.useDuration;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return stack.getItem().isEdible() ? useDuration : 0;
    }

    @Nonnull
    @Override
    public UseAction getUseAnimation(@Nonnull ItemStack stack) {
        return stack.getItem().isEdible() ? useAction : UseAction.NONE;
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull LivingEntity entity) {
        ItemStack itemstack = super.finishUsingItem(stack, world, entity);
        return entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.instabuild ? itemstack : new ItemStack(Items.BOWL);
    }

    public static class Properties extends Item.Properties {
        private UseAction useAction = UseAction.EAT;
        private int useDuration = 32;

        public Properties useAction(UseAction useAction) {
            this.useAction = useAction;
            return this;
        }

        public Properties useDuration(int useDuration) {
            this.useDuration = useDuration;
            return this;
        }
    }
}
