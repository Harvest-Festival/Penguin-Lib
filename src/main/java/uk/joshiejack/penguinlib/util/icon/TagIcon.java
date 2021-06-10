package uk.joshiejack.penguinlib.util.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.renderer.ShadowRenderer;

import java.util.Random;

public class TagIcon extends Icon {
    private static final Random random = new Random(System.currentTimeMillis());
    public static final Icon EMPTY = new ItemIcon(ItemStack.EMPTY);
    private final ITag.INamedTag<Item> tag;
    private ItemStack stack;
    private long timer;
    private int id;

    public TagIcon(ITag.INamedTag<Item> tag) {
        this.tag = tag;
        this.id = random.nextInt(tag.getValues().size());
        this.stack = new ItemStack(tag.getValues().get(id));
        this.timer = System.currentTimeMillis();
    }

    @Override
    public JsonElement toJson(JsonObject json) {
        json.addProperty("tag", tag.getName().toString());
        return json;
    }

    @Override
    public void toNetwork(PacketBuffer pb) {
        pb.writeByte(Icon.Type.TAG.ordinal());
        pb.writeResourceLocation(tag.getName());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(Minecraft mc, MatrixStack matrix, int x, int y) {
        if (System.currentTimeMillis() - timer > 1000) {
            id++;

            if (id >= tag.getValues().size())
                id = 0;
            stack = new ItemStack(tag.getValues().get(id));
            timer = System.currentTimeMillis();
        }

        if (shadowed) ShadowRenderer.enable();
        mc.getItemRenderer().renderGuiItem(stack, x, y);
        if (shadowed) {
            ShadowRenderer.disable();
            shadowed = false;
        }
    }
}
