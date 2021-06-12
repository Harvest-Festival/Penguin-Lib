package uk.joshiejack.penguinlib.note.type;

import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.widget.NoteWidget;
import uk.joshiejack.penguinlib.note.Note;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NoteType {
    public static final Object2ObjectMap<String, NoteType> TYPES = new Object2ObjectOpenHashMap<>();
    public static final NoteType TEXT = new NoteType("text");
    private final String name;

    public NoteType(String text) {
        this.name = text;
        TYPES.put(text, this);
    }

    @Override
    public String toString() {
        return name;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(MatrixStack matrix, NoteWidget widget, int mouseX, int mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.font;
        int j = widget.getFGColor();
        AbstractGui.drawCenteredString(matrix, fontrenderer, widget.getMessage(), widget.x + widget.getWidth() / 2, widget.y, j | MathHelper.ceil(widget.getAlpha() * 255.0F) << 24);
        StringHelper.enableUnicode();
        widget.getChatter().draw(matrix, fontrenderer, widget.x + 2, widget.y + 8, 4210752);
        StringHelper.disableUnicode();
    }

    @OnlyIn(Dist.CLIENT)
    public int getTextWidth() {
        return 165;
    }

    @OnlyIn(Dist.CLIENT)
    public int getLineCount() {
        return 18;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public TextFormatting getTextFormatting() {
        return null;
    }

    @Nonnull
    public ITextComponent getText(Note note) {
        return note.getText();
    }
}