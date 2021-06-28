package uk.joshiejack.penguinlib.note;

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
import uk.joshiejack.penguinlib.network.packet.ReadNotePacket;
import uk.joshiejack.penguinlib.network.packet.UnlockNotePacket;
import uk.joshiejack.penguinlib.note.type.NoteType;
import uk.joshiejack.penguinlib.util.helpers.PlayerHelper;
import uk.joshiejack.penguinlib.util.icon.Icon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Note extends SimplePenguinRecipe {
    private final ITextComponent text;
    private final ITextComponent title;
    private final ResourceLocation category;
    private final NoteType type;
    private boolean isHidden;
    private Icon icon;
    private boolean isLocked;

    public Note(ResourceLocation rl, ResourceLocation category, NoteType type) {
        super(PenguinRegistries.NOTE, PenguinRegistries.NOTE_SERIALIZER.get(), rl, Ingredient.EMPTY, ItemStack.EMPTY);
        this.category = category;
        this.type = type;
        this.text = new TranslationTextComponent(Util.makeDescriptionId("note.text", rl));
        this.title = new TranslationTextComponent(Util.makeDescriptionId("note.title", rl));
    }

    public ITextComponent getTitle() {
        return title;
    }

    public ITextComponent getText() {
        return text;
    }

    public ResourceLocation getCategory() {
        return category;
    }

    public void setHidden() {
        this.isHidden = true;
    }
    public void setLocked() { this.isLocked = true; }

    public Note setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isUnlocked(PlayerEntity player) {
        return PlayerHelper.hasSubTag(player, "Notes", "Unlocked", rl.toString());
    }

    public void unlock(PlayerEntity player) {
        PlayerHelper.setSubTag(player, "Notes", "Unlocked", rl.toString());
        if (!player.level.isClientSide)
            PenguinNetwork.sendToClient(new UnlockNotePacket(this), (ServerPlayerEntity) player);
    }

    public boolean isRead(PlayerEntity player) {
        return PlayerHelper.hasSubTag(player, "Notes", "Read", rl.toString());
    }

    public void read(PlayerEntity player) {
        PlayerHelper.setSubTag(player, "Notes", "Read", rl.toString());
        if (player.level.isClientSide)
            PenguinNetwork.sendToServer(new ReadNotePacket(this));
    }

    public Icon getIcon() {
        return icon;
    }

    public boolean isDefault() {
        return !isLocked;
    }

    public NoteType getNoteType() {
        return type;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<Note> {
        @Nonnull
        @Override
        public Note fromJson(@Nonnull ResourceLocation rl, @Nonnull JsonObject json) {
            NoteType type = json.has("note type") ? NoteType.TYPES.getOrDefault(JSONUtils.getAsString(json, "note type"), NoteType.TEXT) : NoteType.TEXT;
            Note note = new Note(rl, new ResourceLocation(JSONUtils.getAsString(json, "category")), type);
            if (json.has("hidden") && JSONUtils.getAsBoolean(json, "hidden"))
                note.setHidden();
            if (json.has("locked") && JSONUtils.getAsBoolean(json, "locked"))
                note.setLocked();
            note.setIcon(Icon.fromJson(JSONUtils.getAsJsonObject(json, "icon")));
            return note;
        }

        @Nullable
        @Override
        public Note fromNetwork(@Nonnull ResourceLocation rl, @Nonnull PacketBuffer pb) {
            Note note = new Note(rl, pb.readResourceLocation(), NoteType.TYPES.getOrDefault(pb.readUtf(), NoteType.TEXT));
            if (pb.readBoolean())
                note.setHidden();
            if (pb.readBoolean())
                note.setLocked();
            note.icon = Icon.fromNetwork(pb);
            return note;
        }

        @Override
        public void toNetwork(@Nonnull PacketBuffer pb, @Nonnull Note note) {
            pb.writeResourceLocation(note.category);
            pb.writeUtf(note.getNoteType().toString());
            pb.writeBoolean(note.isHidden);
            pb.writeBoolean(note.isLocked);
            note.getIcon().toNetwork(pb);
        }
    }
}