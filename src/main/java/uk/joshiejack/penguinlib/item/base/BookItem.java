package uk.joshiejack.penguinlib.item.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import uk.joshiejack.penguinlib.inventory.AbstractBookContainer;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class BookItem extends Item {
    private final Supplier<IContainerProvider> provider;
    private final Supplier<ContainerType<?>> container;

    public BookItem(Properties properties, Supplier<ContainerType<?>> container) {
        super(properties);
        this.container = container;
        this.provider = null;
    }

    public BookItem(Properties properties, Supplier<IContainerProvider> provider, boolean temporary) {
        super(properties);
        this.container = null;
        this.provider = provider;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if (container != null) return useLegacy(world, player, hand);
        if (!world.isClientSide) {
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider(provider.get(),
                            new TranslationTextComponent(getDescriptionId())));
        }

        return ActionResult.success(player.getItemInHand(hand));
    }

    @Nonnull
    @Deprecated
    public ActionResult<ItemStack> useLegacy(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if (!world.isClientSide) {
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider((id, inv, p) -> container.get().create(id, inv),
                            new TranslationTextComponent(getDescriptionId())));
        }

        return ActionResult.success(player.getItemInHand(hand));
    }
}
