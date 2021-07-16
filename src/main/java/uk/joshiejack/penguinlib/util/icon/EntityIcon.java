package uk.joshiejack.penguinlib.util.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class EntityIcon extends Icon {
    private final EntityType<?> entityType;
    @OnlyIn(Dist.CLIENT)
    private LivingEntity entity;
    private final int scale;

    public EntityIcon(EntityType<?> entityType, int scale) {
        this.entityType = entityType;
        this.scale = scale;
    }

    public JsonElement toJson(JsonObject json) {
        json.addProperty("entity", this.entityType.getRegistryName().toString());
        json.addProperty("scale", scale);
        return json;
    }

    public void toNetwork(PacketBuffer pb) {
        pb.writeByte(Icon.Type.ENTITY.ordinal());
        pb.writeRegistryId(this.entityType);
        pb.writeByte(scale);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(Minecraft mc, MatrixStack matrix, int x, int y) {
        if (this.entity == null) {
            assert mc.level != null;
            Entity test = this.entityType.create(mc.level);
            if (test instanceof LivingEntity)
                this.entity = (LivingEntity) test;
            else
                return;
        }

        InventoryScreen.renderEntityInInventory(x + 8, y + 15, scale, -65F, 0.0F, this.entity);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<ITextComponent> getTooltipLines(PlayerEntity player) {
        List<ITextComponent> list = new ArrayList<>();
        list.add(entityType.getDescription());
        return list;
    }
}
