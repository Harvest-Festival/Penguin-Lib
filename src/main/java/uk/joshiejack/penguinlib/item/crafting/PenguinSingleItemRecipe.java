package uk.joshiejack.penguinlib.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;

public abstract class PenguinSingleItemRecipe extends SingleItemRecipe {
    public PenguinSingleItemRecipe(IRecipeType<?> recipeType, IRecipeSerializer<?> recipeSerializer, ResourceLocation location, String group, Ingredient input, ItemStack output) {
        super(recipeType, recipeSerializer, location, group, input, output);
    }

    public static class Serializer<T extends PenguinSingleItemRecipe> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {
        final IRecipeFactory<T> factory;

        public Serializer(IRecipeFactory<T> factory) {
            this.factory = factory;
        }

        @Nonnull
        @Override
        public T fromJson(@Nonnull ResourceLocation resource, @Nonnull JsonObject json) {
            String s = JSONUtils.getAsString(json, "group", "");
            Ingredient ingredient;
            if (JSONUtils.isArrayNode(json, "ingredient")) {
                ingredient = Ingredient.fromJson(JSONUtils.getAsJsonArray(json, "ingredient"));
            } else {
                ingredient = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "ingredient"));
            }

            String s1 = JSONUtils.getAsString(json, "result");
            int i = JSONUtils.getAsInt(json, "count");
            ItemStack itemstack = new ItemStack(Registry.ITEM.get(new ResourceLocation(s1)), i);
            return this.factory.create(resource, s, ingredient, itemstack);
        }

        @Nonnull
        @Override
        public T fromNetwork(@Nonnull ResourceLocation resource, PacketBuffer buffer) {
            String s = buffer.readUtf(32767);
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack itemstack = buffer.readItem();
            return this.factory.create(resource, s, ingredient, itemstack);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, T recipe) {
            buffer.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }

        public interface IRecipeFactory<T extends SingleItemRecipe> {
            T create(ResourceLocation resource, String name, Ingredient input, ItemStack output);
        }
    }
}
