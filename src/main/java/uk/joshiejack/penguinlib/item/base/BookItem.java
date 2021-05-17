package uk.joshiejack.penguinlib.item.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class BookItem extends Item {
    private final Supplier<ContainerType<?>> container;

    public BookItem(Properties properties, Supplier<ContainerType<?>> container) {
        super(properties);
        this.container = container;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if (!world.isClientSide) {
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider((id, inv, p) -> container.get().create(id, inv),
                    new TranslationTextComponent(getDescriptionId())));
        }

        return ActionResult.success(player.getItemInHand(hand));
    }
}
