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

    public void set(Note note) {
        Minecraft mc = Minecraft.getInstance();
        this.note = note; //Update the stuff
        //this.it = note.getRenderScript() == null ? null : Scripting.get(note.getRenderScript());
        boolean unicode = mc.options.forceUnicodeFont;
        mc.options.forceUnicodeFont = true;
        this.chatter = new Chatter(new StringTextComponent("dsd sds dsddd3wer ere dfsf fsdf gsrert dfg fgdgdfg fsfg gsdfg gsdfgd gdfgdf gdf yt y ghg hgf fdf df sf sdfs df sdf sdg fgfdgdfgdf g fdg dfg fdgd sdfsd fsd fsd fsdf yhfbhfg nghjfg hfg hfgjg jfghfg gfj fgd ffghfgdhjdfg hjgj gj dfg jdfg jdfg jdfg hjgj dfg ujfgjghj yjtyjghjhh")).withWidth(180).withLines(20).withHeight(8).withFormatting(null).setInstant();
        this.chatter.update(mc.font);
        mc.options.forceUnicodeFont = unicode;
        //if (!note.init() && it != null) {
        //  it.callFunction("init", this);
        //}
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        if (note == null) return;
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.font;
        int j = getFGColor();
        drawCenteredString(matrix, fontrenderer, getMessage(), x + width / 2, y, j | MathHelper.ceil(alpha * 255.0F) << 24);
        StringHelper.enableUnicode();
        chatter.draw(matrix, fontrenderer, x + 2, y + 8, 4210752);
        StringHelper.disableUnicode();
    }

    public Note get() {
        return note;
    }
}
