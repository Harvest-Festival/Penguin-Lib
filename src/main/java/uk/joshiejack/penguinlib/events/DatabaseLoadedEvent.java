package uk.joshiejack.penguinlib.events;

import net.minecraftforge.eventbus.api.Event;
import uk.joshiejack.penguinlib.data.database.Table;

import javax.annotation.Nonnull;
import java.util.Map;

public class DatabaseLoadedEvent extends Event {
    private final Map<String, Table> tables;

    public DatabaseLoadedEvent(Map<String, Table> tables) {
        this.tables = tables;
    }

    @Nonnull
    public Table table(String table) {
        return tables.getOrDefault(table, Table.EMPTY);
    }
}
