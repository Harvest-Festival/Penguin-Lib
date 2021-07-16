package uk.joshiejack.penguinlib.util.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class TextureIcon extends Icon {
    private static final List<ITextComponent> EMPTY_LIST = new ArrayList<>();
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
        pb.writeByte(Icon.Type.TEXTURE.ordinal());
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
        mc.gui.setBlitOffset(0);
        mc.gui.blit(matrix, x, y, xPos, shadowed ? yPos + 16 : yPos, 16, 16);
        shadowed = false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<ITextComponent> getTooltipLines(PlayerEntity player) {
        return EMPTY_LIST;
    }
}
