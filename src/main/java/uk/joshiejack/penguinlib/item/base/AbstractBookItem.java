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
import uk.joshiejack.penguinlib.inventory.PenguinContainers;

import javax.annotation.Nonnull;

public class AbstractBookItem extends Item {
    public AbstractBookItem(Properties properties) {
        super(properties);
    }

    protected ContainerType<?> getContainerType() {
        return PenguinContainers.BOOK.get();
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if (!world.isClientSide) {
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider((id, inv, p) -> getContainerType().create(id, inv),
                    new TranslationTextComponent(getDescriptionId())));
        }

        return ActionResult.success(player.getItemInHand(hand));
    }
}
