package uk.joshiejack.penguinlib.client.gui.book.tab;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.page.AbstractPage;
import uk.joshiejack.penguinlib.client.gui.book.page.PageNotes;
import uk.joshiejack.penguinlib.data.PenguinRegistries;
import uk.joshiejack.penguinlib.util.Icon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class NotesTab extends Tab {
    private final List<ResourceLocation> valid = new ArrayList<>();

    public NotesTab(ITextComponent name, Icon icon) {
        super(name, icon);
    }

    public NotesTab withCategory(ResourceLocation resource) {
        valid.add(resource);
        return this;
    }

    @Override
    protected List<AbstractPage> getPages() {
        return Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(PenguinRegistries.CATEGORY).stream()
                .filter(c -> valid.isEmpty() || valid.contains(c.getId()))
                .map(PageNotes::new)
                .collect(Collectors.toList());
    }
}
