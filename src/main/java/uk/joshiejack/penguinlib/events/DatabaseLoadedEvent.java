package uk.joshiejack.penguinlib.events;

import net.minecraft.resources.IResourceManager;
import net.minecraftforge.eventbus.api.Event;
import uk.joshiejack.penguinlib.data.database.Table;

import javax.annotation.Nonnull;
import java.util.Map;

public class DatabaseLoadedEvent extends Event {
    private final Map<String, Table> tables;
    private final IResourceManager rm;

    public DatabaseLoadedEvent(Map<String, Table> tables, IResourceManager rm) {
        this.tables = tables;
        this.rm = rm;
    }

    @Nonnull
    public Table table(String table) {
        return tables.getOrDefault(table, Table.EMPTY);
    }

    public IResourceManager getResourceManager() {
        return rm;
    }
}
