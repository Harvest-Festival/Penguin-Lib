package uk.joshiejack.penguinlib.util.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.renderer.ShadowRenderer;

import java.util.List;

public class ItemIcon extends Icon {
    public static final Icon EMPTY = new ItemIcon(ItemStack.EMPTY);
    private final ItemStack stack;

    public ItemIcon(Item item) {
        this(new ItemStack(item));
    }

    public ItemIcon(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public JsonElement toJson(JsonObject json) {
        json.addProperty("item", stack.getItem().getRegistryName().toString());
        return json;
    }

    @Override
    public void toNetwork(PacketBuffer pb) {
        pb.writeByte(Icon.Type.ITEM.ordinal());
        pb.writeItem(stack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(Minecraft mc, MatrixStack matrix, int x, int y) {
        if (shadowed) ShadowRenderer.enable();
        mc.getItemRenderer().renderGuiItem(stack, x, y);
        if (shadowed) {
            ShadowRenderer.disable();
            shadowed = false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<ITextComponent> getTooltipLines(PlayerEntity player) {
        return stack.getTooltipLines(player, ITooltipFlag.TooltipFlags.NORMAL);
    }
}
