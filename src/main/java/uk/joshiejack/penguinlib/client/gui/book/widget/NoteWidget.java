package uk.joshiejack.penguinlib.client.gui.book.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.Chatter;
import uk.joshiejack.penguinlib.note.Note;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class NoteWidget extends Widget {
    private static final ITextComponent EMPTY = new StringTextComponent("");
    private Chatter chatter;
    private Note note;

    public NoteWidget(int x, int y, int w, int h, NoteWidget previous) {
        super(x, y, w, h, EMPTY);
        if (previous != null && previous.note != null)
            set(previous.note);
    }

    @Nonnull
    @Override
    public ITextComponent getMessage() {
        return note.getTitle();
    }

    public Note getNote() {
        return note;
    }

    public Chatter getChatter() {
        return chatter;
    }

    public float getAlpha() {
        return alpha;
    }

    public void set(@Nonnull Note note) {
        Minecraft mc = Minecraft.getInstance();
        this.note = note; //Update the stuff
        boolean unicode = mc.options.forceUnicodeFont;
        mc.options.forceUnicodeFont = true;
        this.chatter = new Chatter(note.getNoteType().getText(note)).withWidth(note.getNoteType().getTextWidth()).withLines(note.getNoteType().getLineCount()).withHeight(8).withFormatting(note.getNoteType().getTextFormatting()).setInstant();
        this.chatter.update(mc.font);
        mc.options.forceUnicodeFont = unicode;
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        if (note != null)
            note.getNoteType().render(matrix, this, mouseX, mouseY);
    }
}