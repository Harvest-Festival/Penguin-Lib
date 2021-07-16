package uk.joshiejack.penguinlib.util.icon;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.renderer.ShadowRenderer;

import java.util.List;
import java.util.Random;

public abstract class AbstractCyclicIcon<T> extends Icon {
    private static final Random random = new Random(System.currentTimeMillis());
    protected final List<T> list;
    protected T object;
    private long timer;
    private int id;

    public AbstractCyclicIcon(List<T> icons) {
        this.list = icons;
        this.id = list.isEmpty() ? 0 : random.nextInt(list.size());
        this.object = list.isEmpty() ? null : list.get(id);
        this.timer = System.currentTimeMillis();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(Minecraft mc, MatrixStack matrix, int x, int y) {
        if (object == null) return;
        if (System.currentTimeMillis() - timer > 1000) {
            id++;

            if (id >= list.size())
                id = 0;
            object = list.get(id);
            timer = System.currentTimeMillis();
        }

        renderCyclicIcon(mc, matrix, x, y);
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract void renderCyclicIcon(Minecraft mc, MatrixStack matrixStack, int x, int y);

    public abstract static class ItemStack extends AbstractCyclicIcon<net.minecraft.item.ItemStack> {
        public ItemStack(List<net.minecraft.item.ItemStack> list) {
            super(list);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void renderCyclicIcon(Minecraft mc, MatrixStack matrix, int x, int y) {
            if (shadowed) ShadowRenderer.enable();
            mc.getItemRenderer().renderGuiItem(object, x, y);
            if (shadowed) {
                ShadowRenderer.disable();
                shadowed = false;
            }
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public List<ITextComponent> getTooltipLines(PlayerEntity player) {
            return object.getTooltipLines(player, ITooltipFlag.TooltipFlags.NORMAL);
        }
    }
}
