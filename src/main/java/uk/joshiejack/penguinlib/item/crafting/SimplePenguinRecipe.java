package uk.joshiejack.penguinlib.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;

@SuppressWarnings("NullableProblems")
public class SimplePenguinRecipe implements IRecipe<IInventory> {
    protected final ResourceLocation rl;
    protected final Ingredient ingredient;
    protected final ItemStack output;
    protected final IRecipeType<?> type;
    protected final IRecipeSerializer<?> serializer;

    public SimplePenguinRecipe(IRecipeType<?> recipeType, IRecipeSerializer<?> recipeSerializer, ResourceLocation rl, Ingredient ingredient, ItemStack output) {
        this.type = recipeType;
        this.serializer = recipeSerializer;
        this.rl = rl;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean matches(IInventory inventory, @Nonnull World world) {
        return ingredient.test(inventory.getItem(0));
    }

    @Override
    public ItemStack assemble(@Nonnull IInventory inventory) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return rl;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return serializer;
    }

    @Override
    public IRecipeType<?> getType() {
        return type;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    public static class Serializer<T extends SimplePenguinRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {
        final SimplePenguinRecipe.Serializer.IRecipeFactory<T> factory;

        public Serializer(SimplePenguinRecipe.Serializer.IRecipeFactory<T> factory) {
            this.factory = factory;
        }

        @Nonnull
        @Override
        public T fromJson(@Nonnull ResourceLocation resource, @Nonnull JsonObject json) {
            Ingredient ingredient;
            if (JSONUtils.isArrayNode(json, "ingredient")) {
                ingredient = Ingredient.fromJson(JSONUtils.getAsJsonArray(json, "ingredient"));
            } else {
                ingredient = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "ingredient"));
            }

            String s1 = JSONUtils.getAsString(json, "result");
            int i = JSONUtils.getAsInt(json, "count");
            ItemStack itemstack = new ItemStack(Registry.ITEM.get(new ResourceLocation(s1)), i);
            return this.factory.create(resource, ingredient, itemstack);
        }

        @Nonnull
        @Override
        public T fromNetwork(@Nonnull ResourceLocation resource, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack itemstack = buffer.readItem();
            return factory.create(resource, ingredient, itemstack);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, T recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.output);
        }

        public interface IRecipeFactory<T extends SimplePenguinRecipe> {
            T create(ResourceLocation resource, Ingredient input, ItemStack output);
        }
    }
}
