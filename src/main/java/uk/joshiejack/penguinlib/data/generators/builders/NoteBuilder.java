package uk.joshiejack.penguinlib.data.generators.builders;

import com.google.gson.JsonObject;
import uk.joshiejack.penguinlib.data.PenguinRegistries;
import uk.joshiejack.penguinlib.note.Note;

import javax.annotation.Nonnull;

public class NoteBuilder extends SimplePenguinBuilder<Note> {
    private final String category;
    private boolean isHidden;

    public NoteBuilder(String category) {
        super(PenguinRegistries.NOTE_SERIALIZER.get());
        this.category = category;
    }

    public static NoteBuilder note(String category) {
        return new NoteBuilder(category);
    }

    public NoteBuilder setHidden() {
        isHidden = true;
        return this;
    }

    @Override
    public void serializeRecipeData(@Nonnull JsonObject json) {
        json.addProperty("hidden", isHidden);
        json.addProperty("category", category);
    }
}
