package uk.joshiejack.penguinlib.data.generators.builders;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import joptsimple.internal.Strings;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.data.PenguinRegistries;
import uk.joshiejack.penguinlib.note.Note;
import uk.joshiejack.penguinlib.note.type.NoteType;
import uk.joshiejack.penguinlib.util.icon.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class NoteBuilder extends SimplePenguinBuilder<Note> {
    private ResourceLocation category;
    private boolean isHidden;
    private boolean locked;
    private String type;
    private Icon icon = new ItemIcon(ItemStack.EMPTY);
    private CategoryBuilder categoryBuilder;
    public NoteBuilder() {
        super(PenguinRegistries.NOTE_SERIALIZER.get());
    }

    public static NoteBuilder note() {
        return new NoteBuilder();
    }

    public static NoteBuilder note(CategoryBuilder builder) {
        NoteBuilder note = note();
        note.categoryBuilder = builder;
        return note;
    }

    public CategoryBuilder end() {
        return categoryBuilder;
    }

    public NoteBuilder withCategory(CategoryBuilder builder) {
        category = builder.getId();
        return this;
    }

    public NoteBuilder setNoteType(String type) {
        this.type = type;
        return this;
    }

    public NoteBuilder setNoteType(NoteType type) {
        return setNoteType(type.toString());
    }

    public NoteBuilder setHidden() {
        isHidden = true;
        return this;
    }

    public NoteBuilder setLockedByDefault() {
        locked = true;
        return this;
    }

    public NoteBuilder withItemIcon(Item item) {
        icon = new ItemIcon(new ItemStack(item));
        return this;
    }

    public NoteBuilder withTextureIcon(ResourceLocation texture, int x, int y) {
        icon = new TextureIcon(texture, x, y);
        return this;
    }

    public NoteBuilder withPenguinIcon(int x, int y) {
        icon = new TextureIcon(Icon.DEFAULT_LOCATION, x, y);
        return this;
    }

    public NoteBuilder withEntityIcon(EntityType<?> type, int scale) {
        icon = new EntityIcon(type, scale);
        return this;
    }

    public NoteBuilder withNoteIcon() {
        icon = new TextureIcon(Icon.DEFAULT_LOCATION, 0, 0);
        return this;
    }

    public NoteBuilder withTagIcon(ITag.INamedTag<Item> tag) {
        icon = new TagIcon(tag);
        return this;
    }

    public NoteBuilder withListIcon(Item... items) {
        icon = new ItemListIcon(Lists.newArrayList(items).stream().map(ItemStack::new).collect(Collectors.toList()));
        return this;
    }

    @Override
    public void serializeRecipeData(@Nonnull JsonObject json) {
        if (isHidden) json.addProperty("hidden", true);
        if (locked) json.addProperty("locked", true);
        if (!Strings.isNullOrEmpty(type)) json.addProperty("note type", type);
        json.addProperty("category", category.toString());
        json.add("icon", icon.toJson(new JsonObject()));
    }
}
