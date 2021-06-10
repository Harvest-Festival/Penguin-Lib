package uk.joshiejack.penguinlib.util.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.PenguinLib;

public abstract class Icon {
    public static final ResourceLocation DEFAULT_LOCATION = new ResourceLocation(PenguinLib.MODID, "textures/gui/icons.png");
    protected boolean shadowed;

    public enum Type {
        ITEM, TEXTURE, ENTITY, TAG
    }

    public Icon shadowed() {
        this.shadowed = true;
        return this;
    }

    public static Icon fromJson(JsonObject json) {
        return json.has("item") ? new ItemIcon(new ItemStack(JSONUtils.getAsItem(json, "item")))
                : json.has("tag") ? new TagIcon(ItemTags.createOptional(new ResourceLocation(JSONUtils.getAsString(json, "tag"))))
                : json.has("entity") ? new EntityIcon(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(JSONUtils.getAsString(json, "entity"))), JSONUtils.getAsInt(json, "scale"))
                : new TextureIcon(json.has("texture") ? new ResourceLocation(JSONUtils.getAsString(json, "texture")) : DEFAULT_LOCATION,
                json.has("x") ? JSONUtils.getAsInt(json, "x") : 0,
                json.has("y") ? JSONUtils.getAsInt(json, "y") : 0);
    }

    public abstract JsonElement toJson(JsonObject json);

    public static Icon fromNetwork(PacketBuffer pb) {
        Type type = Type.values()[pb.readByte()];
        switch (type) {
            case ITEM:
                return new ItemIcon(new ItemStack(pb.readRegistryIdSafe(Item.class)));
            case TEXTURE:
                return new TextureIcon(pb.readBoolean() ? pb.readResourceLocation() : DEFAULT_LOCATION, pb.readInt(), pb.readInt());
            case ENTITY:
                return new EntityIcon(pb.readRegistryIdSafe(EntityType.class), pb.readByte());
            case TAG:
                return new TagIcon(ItemTags.createOptional(pb.readResourceLocation()));
        }

        //Unreachable
        return null;
    }

    public abstract void toNetwork(PacketBuffer pb);

    @OnlyIn(Dist.CLIENT)
    public abstract void render(Minecraft mc, MatrixStack matrix, int x, int y);
}