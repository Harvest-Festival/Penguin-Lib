package uk.joshiejack.penguinlib.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.renderer.ShadowRenderer;

public abstract class Icon {
    public static final ResourceLocation DEFAULT_LOCATION = new ResourceLocation(PenguinLib.MODID, "textures/gui/icons.png");
    protected boolean shadowed;

    public Icon shadowed() {
        this.shadowed = true;
        return this;
    }


    public static Icon fromJson(JsonObject json) {
        return json.has("item")
                ? new ItemIcon(new ItemStack(JSONUtils.getAsItem(json, "item")))
                : new TextureIcon(json.has("texture") ? new ResourceLocation(JSONUtils.getAsString(json, "texture")) : DEFAULT_LOCATION,
                JSONUtils.getAsInt(json, "x"),
                JSONUtils.getAsInt(json, "y"));
    }

    public abstract JsonElement toJson(JsonObject json);

    public static Icon fromNetwork(PacketBuffer pb) {
        return pb.readBoolean() ? new ItemIcon(new ItemStack(pb.readRegistryIdSafe(Item.class)))
                : new TextureIcon(pb.readBoolean() ? pb.readResourceLocation() : DEFAULT_LOCATION, pb.readInt(), pb.readInt());
    }

    public abstract void toNetwork(PacketBuffer pb);

    @OnlyIn(Dist.CLIENT)
    public abstract void render(Minecraft mc, MatrixStack matrix, int x, int y);

    public static class ItemIcon extends Icon {
        public static final Icon EMPTY = new Icon.ItemIcon(ItemStack.EMPTY);
        private final ItemStack stack;

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
            pb.writeBoolean(true);
            pb.writeRegistryId(stack.getItem());
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
    }

    public static class TextureIcon extends Icon {
        private final ResourceLocation texture;
        private final int xPos;
        private final int yPos;

        public TextureIcon(ResourceLocation texture, int x, int y) {
            this.texture = texture;
            this.xPos = x;
            this.yPos = y;
        }

        @Override
        public JsonElement toJson(JsonObject json) {
            if (!texture.equals(DEFAULT_LOCATION))
                json.addProperty("texture", texture.toString());
            json.addProperty("x", xPos);
            json.addProperty("y", yPos);
            return json;
        }

        @Override
        public void toNetwork(PacketBuffer pb) {
            pb.writeBoolean(false);
            if (texture.equals(DEFAULT_LOCATION))
                pb.writeBoolean(false);
            else {
                pb.writeBoolean(true);
                pb.writeResourceLocation(texture);
            }

            pb.writeInt(xPos);
            pb.writeInt(yPos);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void render(Minecraft mc, MatrixStack matrix, int x, int y) {
            mc.getTextureManager().bind(texture);
            mc.gui.blit(matrix, x, y, xPos, shadowed ? yPos + 16 : yPos, 16, 16);
            shadowed = false;
        }
    }
}
