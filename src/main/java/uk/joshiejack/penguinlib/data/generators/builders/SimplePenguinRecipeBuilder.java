package uk.joshiejack.penguinlib.data.generators.builders;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@SuppressWarnings("NullableProblems")
public class SimplePenguinRecipeBuilder {
    protected final Item result;
    protected final Ingredient ingredient;
    protected final int count;
    protected final Advancement.Builder advancement = Advancement.Builder.advancement();
    protected final IRecipeSerializer<?> type;

    public SimplePenguinRecipeBuilder(IRecipeSerializer<?> serializer, Ingredient ingredient, IItemProvider output, int amount) {
        this.type = serializer;
        this.result = output.asItem();
        this.ingredient = ingredient;
        this.count = amount;
    }

    public SimplePenguinRecipeBuilder unlocks(String name, ICriterionInstance criterion) {
        this.advancement.addCriterion(name, criterion);
        return this;
    }

    public void save(Consumer<IFinishedRecipe> consumer, String name) {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
        if ((new ResourceLocation(name)).equals(resourcelocation)) {
            throw new IllegalStateException("Single Item Recipe " + name + " should remove its 'save' argument");
        } else {
            this.save(consumer, new ResourceLocation(name));
        }
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation resource) {
        ensureValid(resource);
        advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resource)).rewards(AdvancementRewards.Builder.recipe(resource)).requirements(IRequirementsStrategy.OR);
        consumer.accept(accept(resource));
    }

    protected IFinishedRecipe accept(ResourceLocation resource) {
        assert this.result.getItemCategory() != null;
        return new Result(resource, this.type, this.ingredient, this.result, this.count, this.advancement,
                new ResourceLocation(resource.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + resource.getPath()));
    }

    protected void ensureValid(ResourceLocation p_218646_1_) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + p_218646_1_);
        }
    }

    @SuppressWarnings("deprecation")
    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final Item result;
        private final int count;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final IRecipeSerializer<?> type;

        public Result(ResourceLocation rl, IRecipeSerializer<?> serializer, Ingredient ingredient, Item output, int count,
                      Advancement.Builder advancementBuilder, ResourceLocation advancementID) {
            this.id = rl;
            this.type = serializer;
            this.ingredient = ingredient;
            this.result = output;
            this.count = count;
            this.advancement = advancementBuilder;
            this.advancementId = advancementID;
        }

        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", this.ingredient.toJson());
            json.addProperty("result", Registry.ITEM.getKey(this.result).toString());
            json.addProperty("count", this.count);
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public IRecipeSerializer<?> getType() {
            return this.type;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}