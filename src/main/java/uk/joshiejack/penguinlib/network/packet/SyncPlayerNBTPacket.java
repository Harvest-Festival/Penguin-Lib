package uk.joshiejack.penguinlib.network.packet;

import joptsimple.internal.Strings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public abstract class SyncPlayerNBTPacket extends AbstractSyncCompoundNBTPacket {
    private final String tagName;

    public SyncPlayerNBTPacket(){ this.tagName = Strings.EMPTY; } //Ignored
    public SyncPlayerNBTPacket(String tagName) { this.tagName = tagName; }
    public SyncPlayerNBTPacket(String tagName, CompoundNBT tag) {
        super(tag);
        this.tagName = tagName;
    }

    @Override
    public void handle(PlayerEntity player) {
       player.getPersistentData().put(tagName, tag);
    }
}
