package uk.joshiejack.penguinlib.data.generators.builders;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@SuppressWarnings("NullableProblems")
public class SimplePenguinRegistryBuilder <T extends IRecipe<IInventory>> implements IFinishedRecipe {
    private final Ingredient ingredient;
    private final IRecipeSerializer<T> type;
    private ResourceLocation id;

    public SimplePenguinRegistryBuilder(IRecipeSerializer<T> serializer, Ingredient ingredient) {
        this.type = serializer;
        this.ingredient = ingredient;
    }

    public void serializeRecipeData(JsonObject json) {
        json.add("ingredient", this.ingredient.toJson());
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public IRecipeSerializer<?> getType() {
        return this.type;
    }

    @Nullable
    public JsonObject serializeAdvancement() {
        return null;
    }

    @Nullable
    public ResourceLocation getAdvancementId() {
        return null;
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation resource) {
        this.id = resource;
        consumer.accept(this);
    }
}