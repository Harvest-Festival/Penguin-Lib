package uk.joshiejack.penguinlib.util.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.stream.Collectors;

public class TagIcon extends AbstractCyclicIcon.ItemStack {
    private final ITag.INamedTag<Item> tag;

    public TagIcon(ITag.INamedTag<Item> tag) {
        super(tag.getValues().stream().map(net.minecraft.item.ItemStack::new).collect(Collectors.toList()));
        this.tag = tag;

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
    public List<ITextComponent> getTooltipLines(PlayerEntity player) {
        return object.getTooltipLines(player, ITooltipFlag.TooltipFlags.NORMAL);
    }
}