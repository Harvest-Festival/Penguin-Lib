package uk.joshiejack.penguinlib.note;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import uk.joshiejack.penguinlib.data.PenguinRegistries;
import uk.joshiejack.penguinlib.item.crafting.SimplePenguinRecipe;
import uk.joshiejack.penguinlib.util.icon.Icon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Category extends SimplePenguinRecipe {
    private String title;
    private Icon icon;

    public Category(ResourceLocation rl) {
        super(PenguinRegistries.CATEGORY, PenguinRegistries.CATEGORY_SERIALIZER.get(), rl, Ingredient.EMPTY, ItemStack.EMPTY);
        this.title = Util.makeDescriptionId("note.category", rl);
    }

    public ITextComponent getTitle() {
        return new TranslationTextComponent(title);
    }

    public Category setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public Icon getIcon() {
        return icon;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<Category> {
        @Nonnull
        @Override
        public Category fromJson(@Nonnull ResourceLocation rl, @Nonnull JsonObject json) {
            Category category = new Category(rl);
            category.setIcon(Icon.fromJson(JSONUtils.getAsJsonObject(json, "icon")));
            return category;
        }

        @Nullable
        @Override
        public Category fromNetwork(@Nonnull ResourceLocation rl, @Nonnull PacketBuffer pb) {
            Category category = new Category(rl);
            category.icon = Icon.fromNetwork(pb);
            return category;
        }

        @Override
        public void toNetwork(@Nonnull PacketBuffer pb, @Nonnull Category category) {
            category.getIcon().toNetwork(pb);
        }
    }
}