package uk.joshiejack.penguinlib.data;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.note.Category;
import uk.joshiejack.penguinlib.note.Note;

public class PenguinRegistries {
    public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PenguinLib.MODID);
    public static final RegistryObject<Database.Serializer> DATABASE_SERIALIZER = SERIALIZERS.register("database", Database.Serializer::new);
    public static final RegistryObject<Note.Serializer> NOTE_SERIALIZER = SERIALIZERS.register("note", Note.Serializer::new);
    public static final RegistryObject<Category.Serializer> CATEGORY_SERIALIZER = SERIALIZERS.register("category", Category.Serializer::new);
    public static final IRecipeType<Database.Dummy> DATABASE = IRecipeType.register(PenguinLib.MODID + ":database");
    public static final IRecipeType<Note> NOTE = IRecipeType.register(PenguinLib.MODID + ":notes");
    public static final IRecipeType<Category> CATEGORY = IRecipeType.register(PenguinLib.MODID + ":categories");
}
