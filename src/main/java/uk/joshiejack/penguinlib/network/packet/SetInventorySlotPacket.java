package uk.joshiejack.penguinlib.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import uk.joshiejack.penguinlib.tile.inventory.AbstractInventoryTileEntity;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SetInventorySlotPacket extends BlockRenderUpdatePacket {
    private int slot;
    private ItemStack stack;

    public SetInventorySlotPacket() {}
    public SetInventorySlotPacket(BlockPos pos, int slot, ItemStack stack) {
        super(pos);
        this.slot = slot;
        this.stack = stack;
    }

    @Override
    public void encode(PacketBuffer to) {
        super.encode(to);
        to.writeInt(slot);
        to.writeItemStack(stack, false);
    }

    @Override
    public void decode(PacketBuffer from) {
        super.decode(from);
        slot = from.readInt();
        stack = from.readItem();
    }

    @Override
    public void handle(PlayerEntity player) {
        TileEntity tile = player.level.getBlockEntity(pos);
        if (tile instanceof AbstractInventoryTileEntity) {
            LazyOptional<IItemHandler> handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            handler.ifPresent(itemHandler -> {
                if (itemHandler instanceof ItemStackHandler) {
                    ((ItemStackHandler) itemHandler).setStackInSlot(slot, stack);
                }
            });
        }

        super.handle(player);
    }
}
