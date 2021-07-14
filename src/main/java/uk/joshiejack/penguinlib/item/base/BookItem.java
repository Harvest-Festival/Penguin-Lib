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

    public BookItem(Properties properties, Supplier<IContainerProvider> provider) {
        this(properties, provider, false);
    }

    @Deprecated //TODO: Remove in 0.6+
    public BookItem(Properties properties, Supplier<IContainerProvider> provider, boolean temporary) {
        super(properties);
        this.provider = provider;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if (!world.isClientSide) {
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider(provider.get(),
                            new TranslationTextComponent(getDescriptionId())));
        }

        return ActionResult.success(player.getItemInHand(hand));
    }
}
