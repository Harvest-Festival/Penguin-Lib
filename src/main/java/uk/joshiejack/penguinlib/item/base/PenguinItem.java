package uk.joshiejack.penguinlib.item.base;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class PenguinItem extends Item {
    private final Supplier<ItemStack> result;
    private final UseAction useAction;
    private final int useDuration;
    private final BiConsumer<ItemStack, LivingEntity> finisher;

    public PenguinItem(Item.Properties properties) {
        super(properties);
        Properties pp = properties instanceof Properties ? ((Properties)properties) : null;
        this.useAction = pp == null ? UseAction.NONE : pp.useAction;
        this.useDuration = pp == null ? 0 : pp.useDuration;
        this.result = pp == null  || pp.result == null ? null : pp.result;
        this.finisher = pp == null ? null : pp.consumer;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return stack.getItem().isEdible() ? useDuration : 0;
    }

    public ItemStack getLeftovers() {
        return result.get();
    }

    @Nonnull
    @Override
    public UseAction getUseAnimation(@Nonnull ItemStack stack) {
        return stack.getItem().isEdible() ? useAction : UseAction.NONE;
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull LivingEntity entity) {
        if (finisher != null) finisher.accept(stack, entity); //Do special stuff
        if (result == null || (!(entity instanceof PlayerEntity))) return super.finishUsingItem(stack, world, entity);
        super.finishUsingItem(stack, world, entity);
        if (stack.isEmpty())
            return result.get();
        else
            ItemHandlerHelper.giveItemToPlayer((PlayerEntity) entity, result.get());
        return stack;
    }

    public static class Properties extends Item.Properties {
        public BiConsumer<ItemStack, LivingEntity> consumer;
        private Supplier<ItemStack> result;
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

        public Properties withContainer(Supplier<ItemStack> result) {
            this.result = result;
            return this;
        }

        public Properties finishUsing(BiConsumer<ItemStack, LivingEntity> consumer) {
            this.consumer = consumer;
            return this;
        }
    }
}
