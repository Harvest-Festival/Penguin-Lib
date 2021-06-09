package uk.joshiejack.penguinlib.note;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.UnlockNotePacket;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Note extends SimplePenguinRecipe {
    public static final Multimap<String, Note> BY_CATEGORY = LinkedHashMultimap.create();
    private String unlocalized = null;
    private final String category;
    private boolean isHidden;

    public Note(ResourceLocation rl, String category) {
        super(PenguinRegistries.NOTE, PenguinRegistries.NOTE_SERIALIZER.get(), rl, Ingredient.EMPTY, ItemStack.EMPTY);
        this.category = category;
        Note.BY_CATEGORY.get(category).add(this);
    }

    protected String getOrCreateUnlocalizedText() {
        if (this.unlocalized == null) {
            this.unlocalized = Util.makeDescriptionId("note", rl);
        }

        return this.unlocalized;
    }

    public ITextComponent getText() {
        return new TranslationTextComponent(getOrCreateUnlocalizedText());
    }

    public Note setHidden() {
        this.isHidden = true;
        return this;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isUnlocked(PlayerEntity player) {
        return PlayerHelper.hasTag(player, "Notes", rl.toString());
    }

    public void unlock(PlayerEntity player) {
        PlayerHelper.setTag(player, "Notes", rl.toString());
        if (!player.level.isClientSide)
            PenguinNetwork.sendToClient(new UnlockNotePacket(this), (ServerPlayerEntity) player);
    }

    public boolean isRead(PlayerEntity player) {
        return PlayerHelper.hasTag(player, "ReadNotes", rl.toString());
    }

    public void read(PlayerEntity player) {
        PlayerHelper.setTag(player, "ReadNotes", rl.toString());
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<Note> {
        @Nonnull
        @Override
        public Note fromJson(@Nonnull ResourceLocation rl, @Nonnull JsonObject json) {
            Note note = new Note(rl, JSONUtils.getAsString(json, "category"));
            if (JSONUtils.getAsBoolean(json, "hidden"))
                note.setHidden();
            return note;
        }

        @Nullable
        @Override
        public Note fromNetwork(@Nonnull ResourceLocation rl, @Nonnull PacketBuffer pb) {
            Note note = new Note(rl, pb.readUtf());
            if (pb.readBoolean())
                note.setHidden();
            return note;
        }

        @Override
        public void toNetwork(@Nonnull PacketBuffer pb, @Nonnull Note note) {
            pb.writeUtf(note.category);
            pb.writeBoolean(note.isHidden);
        }
    }
}