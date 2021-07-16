package uk.joshiejack.penguinlib.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.PenguinClientConfig;
import uk.joshiejack.penguinlib.util.PenguinTags;
import uk.joshiejack.penguinlib.util.helpers.PlayerHelper;
import uk.joshiejack.penguinlib.util.helpers.TimeHelper;

import javax.annotation.Nullable;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PenguinLib.MODID)
public class HUDRenderer {
    public static Object2ObjectMap<RegistryKey<World>, HUDRenderData> RENDERERS = new Object2ObjectOpenHashMap<>();

    public abstract static class HUDRenderData {
        public boolean isEnabled(Minecraft mc) { return true; }

        @Nullable
        public ResourceLocation getTexture(Minecraft mc) { return null; }
        public abstract ITextComponent getHeader(Minecraft mc);
        public String getFooter(Minecraft mc) {
            String time = formatTime((int) TimeHelper.getTimeOfDay(mc.level.getDayTime()));
            return "(" + TimeHelper.shortName(TimeHelper.getWeekday(mc.level.getDayTime())) + ")" + "  " + time;
        }

        public int getX() { return 0; }
        public int getY() { return 0; }
    }

    private static String formatTime(int time) {
        int hour = time / 1000;
        int minute = (int) ((double) (time % 1000) / 20 * 1.2);
        if (PenguinClientConfig.clockType.get() == PenguinClientConfig.ClockType.TWENTY_FOUR_HOUR) {
            return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
        } else {
            boolean pm = false;
            if (hour > 12) {
                hour = hour - 12;
                pm = true;
            }
            if (hour == 12)
                pm = true;
            if (hour == 0)
                hour = 12;

            return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + (pm ? "PM" : "AM");
        }
    }

    private static boolean hasClockInventory;

    private static boolean hasClockInInventory(PlayerEntity player) {
        if (player.level.getDayTime() % 60 == 0)
            hasClockInventory = PlayerHelper.hasInInventory(player, Ingredient.of(PenguinTags.CLOCKS), 1);
        return hasClockInventory;
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (RENDERERS.size() == 0) return;
        Minecraft mc = Minecraft.getInstance();
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            HUDRenderData hud = RENDERERS.get(mc.level.dimension());
            if (hud != null) {
                MatrixStack matrix = event.getMatrixStack();
                RenderSystem.enableBlend();
                int x = hud.getX();
                int y = hud.getY();
                if (hud.isEnabled(mc)) {
                    ResourceLocation texture = hud.getTexture(mc);
                    if (texture != null) {
                        RenderSystem.color4f(1F, 1F, 1F, 1F);
                        mc.getTextureManager().bind(texture);//inMine ? MINE_HUD : season.HUD);
                        mc.gui.blit(matrix, x - 44, y - 35, 0, 0, 256, 110);
                    }

                    //Enlarge the Day
                    matrix.pushPose();
                    matrix.scale(1.4F, 1.4F, 1.4F);
                    ITextComponent header = hud.getHeader(mc);
                    mc.font.drawShadow(matrix, header, (x / 1.4F) + 30, (y / 1.4F) + 7, 0xFFFFFFFF);
                    matrix.popPose();
                }

                //Draw the time
                if (PenguinClientConfig.displayClockInHUDs.get()) {
                    if (!PenguinClientConfig.requireClockItemForTime.get() || (PenguinClientConfig.requireClockItemForTime.get() && hasClockInInventory(Objects.requireNonNull(mc.player))))
                        mc.font.drawShadow(matrix, hud.getFooter(mc), x + 42, y + 23, 0xFFFFFFFF);
                }
                RenderSystem.disableBlend();
            }
        }
    }
}