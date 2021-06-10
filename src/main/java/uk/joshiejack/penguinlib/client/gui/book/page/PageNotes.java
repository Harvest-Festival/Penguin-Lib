package uk.joshiejack.penguinlib.client.gui.book.page;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.client.gui.book.widget.NoteButton;
import uk.joshiejack.penguinlib.client.gui.book.widget.NoteWidget;
import uk.joshiejack.penguinlib.data.PenguinRegistries;
import uk.joshiejack.penguinlib.note.Category;
import uk.joshiejack.penguinlib.note.Note;
import uk.joshiejack.penguinlib.util.Icon;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class PageNotes extends AbstractMultiPage.Right<Note> {
    private final Category category;
    private Predicate<Note> filter = (n) -> true;
    public NoteWidget note;

    public PageNotes(Category category) {
        super(category.getTitle(), 56);
        this.category = category;
    }

    @Override
    protected Icon getIcon() {
        return category.getIcon();
    }

    @Override
    public void initLeft(Book book, int left, int top) {
        note = new NoteWidget(left + 18, top, 130, 20, note);
        book.addButton(note);
    }

    @Override
    protected void initEntry(Book book, int left, int top, int id, Note note) {
        book.addButton(new NoteButton(this.note.get(), note, left + 8 + ((id % 7) * 18), top + 8 + ((id / 7) * 18),
                (button) -> {
                    if (note.isDefault() || note.isUnlocked(Minecraft.getInstance().player)) {
                        this.note.set(note); //Set the page to this and mark as read
                        note.read(Minecraft.getInstance().player);
                    }
                },
                createTooltip(book, note.getTitle())));
    }

    @Override
    protected List<Note> getEntries() {
        PlayerEntity player = Minecraft.getInstance().player;
        assert player != null;
        return player.level.getRecipeManager().getAllRecipesFor(PenguinRegistries.NOTE).stream()
                .filter(n -> n.getCategory().equals(category.getId()))
                .filter(n -> this.filter.test(n))
                .filter(n -> !n.isHidden() || n.isUnlocked(player) || n.isDefault())
                .collect(Collectors.toList());
    }
}
