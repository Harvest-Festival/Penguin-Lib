package uk.joshiejack.penguinlib.data.generators.builders;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.data.PenguinRegistries;
import uk.joshiejack.penguinlib.note.Category;
import uk.joshiejack.penguinlib.util.Icon;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class CategoryBuilder extends SimplePenguinBuilder<Category> {
    private Icon icon = new Icon.ItemIcon(ItemStack.EMPTY);
    public CategoryBuilder() {
        super(PenguinRegistries.CATEGORY_SERIALIZER.get());
    }
    private List<Pair<String, NoteBuilder>> notes = new ArrayList<>();

    public static CategoryBuilder category() {
        return new CategoryBuilder();
    }

    public CategoryBuilder withItemIcon(Item item) {
        icon = new Icon.ItemIcon(new ItemStack(item));
        return this;
    }

    public CategoryBuilder withTextureIcon(ResourceLocation texture, int x, int y) {
        icon = new Icon.TextureIcon(texture, x, y);
        return this;
    }

    public CategoryBuilder withPenguinIcon(int x, int y) {
        icon = new Icon.TextureIcon(Icon.DEFAULT_LOCATION, x, y);
        return this;
    }

    public CategoryBuilder withEntityIcon(EntityType<?> type) {
        icon = new Icon.EntityIcon(type);
        return this;
    }

    public CategoryBuilder withNoteIcon() {
        icon = new Icon.TextureIcon(Icon.DEFAULT_LOCATION, 0, 0);
        return this;
    }

    public NoteBuilder withNote(String name) {
        NoteBuilder builder = NoteBuilder.note(this);
        notes.add(Pair.of(name, builder));
        return builder;
    }

    @Override
    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation resource) {
        super.save(consumer, resource);
        notes.forEach(pair -> pair.getRight().withCategory(this).save(consumer, new ResourceLocation(resource.getNamespace(), pair.getLeft())));
    }

    @Override
    public void serializeRecipeData(@Nonnull JsonObject json) {
        json.add("icon", icon.toJson(new JsonObject()));
    }
}
