package uk.joshiejack.penguinlib.util.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
            this.entity = (LivingEntity) this.entityType.create(mc.level);
        }

        assert this.entity != null;

        InventoryScreen.renderEntityInInventory(x + 8, y + 15, scale, -65F, 0.0F, this.entity);
    }
}
