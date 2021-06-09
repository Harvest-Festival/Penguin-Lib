package uk.joshiejack.penguinlib.data.generators.builders;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@SuppressWarnings("NullableProblems")
public abstract class SimplePenguinBuilder<T extends IRecipe<IInventory>> implements IFinishedRecipe {
    private final IRecipeSerializer<T> type;
    private ResourceLocation id;

    public SimplePenguinBuilder(IRecipeSerializer<T> serializer) {
        this.type = serializer;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getType() {
        return this.type;
    }

    @Override
    @Nullable
    public JsonObject serializeAdvancement() {
        return null;
    }

    @Override
    @Nullable
    public ResourceLocation getAdvancementId() {
        return null;
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation resource) {
        this.id = resource;
        consumer.accept(this);
    }
}