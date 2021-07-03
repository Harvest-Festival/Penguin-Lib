package uk.joshiejack.penguinlib.util.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.List;

public class ItemListIcon extends AbstractCyclicIcon.ItemStack {
    public static final Icon EMPTY = new ItemListIcon(new ArrayList<>());

    public ItemListIcon(List<net.minecraft.item.ItemStack> stacks) {
        super(stacks);
    }

    @Override
    public JsonElement toJson(JsonObject json) {
        JsonArray array = new JsonArray();
        for (net.minecraft.item.ItemStack stack: list) {
            array.add(stack.getItem().getRegistryName().toString());
        }

        json.add("list", array);
        return json;
    }

    @Override
    public void toNetwork(PacketBuffer pb) {
        pb.writeByte(Type.ITEM_LIST.ordinal());
        pb.writeShort(list.size());
        list.forEach(pb::writeItem);
    }

}