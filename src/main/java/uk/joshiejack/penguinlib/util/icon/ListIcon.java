package uk.joshiejack.penguinlib.util.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import uk.joshiejack.penguinlib.client.renderer.ShadowRenderer;

import java.util.ArrayList;
import java.util.List;

public class ListIcon extends AbstractCyclicIcon<Icon> {
    public static final Icon EMPTY = new ListIcon(new ArrayList<>());

    public ListIcon(List<Icon> icons) {
        super(icons);
    }

    @Override
    public JsonElement toJson(JsonObject json) {
        JsonArray array = new JsonArray();
        for (Icon icon: list) {
            JsonObject data = new JsonObject();
            icon.toJson(data);
            array.add(data);
        }

        json.add("icon_list", array);
        return json;
    }

    @Override
    public void toNetwork(PacketBuffer pb) {
        pb.writeByte(Type.ITEM_LIST.ordinal());
        pb.writeShort(list.size());
        list.forEach(icon -> icon.toNetwork(pb));
    }

    @Override
    protected void renderCyclicIcon(Minecraft mc, MatrixStack matrixStack, int x, int y) {
        if (shadowed) ShadowRenderer.enable();
        object.render(mc, matrixStack, x, y);
        if (shadowed) {
            ShadowRenderer.disable();
            shadowed = false;
        }
    }
}