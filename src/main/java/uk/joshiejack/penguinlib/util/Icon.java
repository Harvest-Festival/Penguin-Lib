package uk.joshiejack.penguinlib.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
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
        return json.has("item") ? new ItemIcon(new ItemStack(JSONUtils.getAsItem(json, "item")))
                : json.has("entity") ? new EntityIcon(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(JSONUtils.getAsString(json, "entity"))))
                : new TextureIcon(json.has("texture") ? new ResourceLocation(JSONUtils.getAsString(json, "texture")) : DEFAULT_LOCATION,
                json.has("x") ? JSONUtils.getAsInt(json, "x") : 0,
                json.has("y") ? JSONUtils.getAsInt(json, "y") : 0);
    }

    public abstract JsonElement toJson(JsonObject json);

    public static Icon fromNetwork(PacketBuffer pb) {
        int type = pb.readByte();
        return type == 1 ? new ItemIcon(new ItemStack(pb.readRegistryIdSafe(Item.class)))
                : type == 3 ? new EntityIcon(pb.readRegistryIdSafe(EntityType.class))
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
            pb.writeByte(1);
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
            if (xPos != 0)
                json.addProperty("x", xPos);
            if (yPos != 0)
                json.addProperty("y", yPos);
            return json;
        }

        @Override
        public void toNetwork(PacketBuffer pb) {
            pb.writeByte(2);
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

    public static class EntityIcon extends Icon {
        private final EntityType<?> entityType;
        @OnlyIn(Dist.CLIENT)
        private LivingEntity entity;

        public EntityIcon(EntityType<?> entityType) {
            this.entityType = entityType;
        }

        @Override
        public JsonElement toJson(JsonObject json) {
            json.addProperty("entity", entityType.getRegistryName().toString());
            return json;
        }

        @Override
        public void toNetwork(PacketBuffer pb) {
            pb.writeByte(3);
            pb.writeRegistryId(entityType);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void render(Minecraft mc, MatrixStack matrix, int x, int y) {
            if (entity == null) entity = (LivingEntity) entityType.create(mc.level);
            assert entity != null;
            InventoryScreen.renderEntityInInventory(x, y, 10, 45F, 0F, entity);
        }
    }
}