package uk.joshiejack.penguinlib.block.base;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class AbstractInventoryBlock extends AbstractPenguinBlock {
    public AbstractInventoryBlock(net.minecraft.block.AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    protected ActionResultType insert(PlayerEntity player, Hand hand, IItemHandler handler) {
        ItemStack held = player.getItemInHand(hand).copy();
        ItemStack ret = ItemHandlerHelper.insertItem(handler, held, false);
        if (ret.getCount() != held.getCount() || ret.isEmpty()) {
            if (!player.isCreative())
                player.setItemInHand(hand, ret);
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    protected int getExtractAmount(IItemHandler handler, int slot) {
        return 1;
    }

    protected ActionResultType extract(PlayerEntity player, IItemHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            int extract = getExtractAmount(handler, i);
            if (extract > 0) {
                ItemStack extracted = handler.extractItem(i, extract, false);
                if (!extracted.isEmpty()) {
                    ItemHandlerHelper.giveItemToPlayer(player, extracted);
                    return ActionResultType.SUCCESS; //Successfully extracted an item
                }
            }
        }

        return ActionResultType.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult blockRayTraceResult) {
        TileEntity tileentity = world.getBlockEntity(pos);
        if (tileentity == null || player.isShiftKeyDown())
            return ActionResultType.FAIL;
        else {
            LazyOptional<IItemHandler> optional = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, blockRayTraceResult.getDirection());
            if (optional.isPresent()) {
                IItemHandler handler = optional.resolve().get();
                return !player.getItemInHand(hand).isEmpty() && insert(player, hand, handler) == ActionResultType.SUCCESS ? ActionResultType.SUCCESS : extract(player, handler);
            }

            return ActionResultType.PASS;
        }
    }

    protected void onRemoved(IItemHandler handler, World world, BlockPos pos) {
        NonNullList<ItemStack> list = NonNullList.withSize(handler.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack inSlot = handler.getStackInSlot(i);
            if (!inSlot.isEmpty())
                list.set(i, inSlot);
        }

        InventoryHelper.dropContents(world, pos, list);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState oldState, @Nonnull World world, @Nonnull BlockPos pos, BlockState newState, boolean p_196243_5_) {
        if (!oldState.is(newState.getBlock())) {
            TileEntity tileentity = world.getBlockEntity(pos);
            if (tileentity != null) {
                LazyOptional<IItemHandler> optional = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                if (optional.isPresent()) {
                    onRemoved(optional.resolve().get(), world, pos);
                    world.updateNeighbourForOutputSignal(pos, this);
                }
            }

            super.onRemove(oldState, world, pos, newState, p_196243_5_);
        }
    }
}
