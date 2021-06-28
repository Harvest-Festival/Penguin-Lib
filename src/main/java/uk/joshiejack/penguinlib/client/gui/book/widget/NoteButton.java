package uk.joshiejack.penguinlib.client.gui.book.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.note.Note;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class NoteButton extends AbstractButton {
    private final Note note;
    private final Note selected;

    public NoteButton(Book book, Note selected, Note note, int x, int y, IPressable pressable, ITooltip tooltip) {
        super(book, x, y, 16, 16, note.getTitle(), pressable, tooltip);
        this.note = note;
        this.selected = selected;
    }

    @Override
    protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        boolean unlocked = note.isDefault() || note.isUnlocked(player);
        if (note.equals(selected)) fill(matrix, x, y, x + width, y + height, 0x559C8C63);
        else if (!hovered || !unlocked) fill(matrix, x, y, x + width, y + height, 0x55B0A483);
        else fill(matrix, x, y, x + width, y + height, 0x55C4B9A2);

        mc.gui.setBlitOffset(100);
        (unlocked ? note.getIcon() : note.getIcon().shadowed()).render(Minecraft.getInstance(), matrix, x, y);
        if (unlocked && !note.isRead(player)) {
            StringHelper.enableUnicode();
            matrix.pushPose();
            matrix.translate(0D, 0D, 110D);
            mc.font.drawShadow(matrix, TextFormatting.BOLD + "NEW", x + 1, y + 8, 0xFFFFFFFF);
            matrix.popPose();
            StringHelper.disableUnicode();
        }
    }

    @Override
    public void renderToolTip(@Nonnull MatrixStack mtx, int mouseX, int mouseY) {
        if (note.isDefault() || note.isUnlocked(Minecraft.getInstance().player))
            super.renderToolTip(mtx, mouseX, mouseY);
    }
}