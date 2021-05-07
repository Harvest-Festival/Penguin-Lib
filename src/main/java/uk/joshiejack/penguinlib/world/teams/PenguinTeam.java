package uk.joshiejack.penguinlib.world.teams;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.penguinlib.events.TeamChangedOwnerEvent;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.SyncTeamDataPacket;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

public class PenguinTeam implements INBTSerializable<CompoundNBT> {
    private boolean isClient;
    private CompoundNBT data;
    private Set<UUID> members;
    private UUID teamUUID;
    private UUID owner;

    public PenguinTeam(UUID uuid) {
        this.teamUUID = uuid;
        this.members = Sets.newHashSet();
        this.data = new CompoundNBT();
    }

    public void setClient() {
        this.isClient = true;
    }

    public boolean isClient() {
        return isClient;
    }

    public PenguinTeam onChanged(ServerWorld world) {
        if (!members.contains(owner)) {
            this.owner = members.stream().findFirst().orElse(null); //Grab a new one, it can be null
            MinecraftForge.EVENT_BUS.post(new TeamChangedOwnerEvent(teamUUID, owner));
        }

        syncToTeam(world);
        return this;
    }

    public PenguinTeam(CompoundNBT data) {
        this.deserializeNBT(data);
    }

    public Set<UUID> members() {
        return members;
    }

    @Nullable
    public UUID getOwner() {
        return owner;
    }

    public UUID getID() {
        return teamUUID;
    }

    public CompoundNBT getData() {
        return data;
    }

    public void syncToPlayer(ServerPlayerEntity player) {
        PenguinNetwork.sendToClient(new SyncTeamDataPacket(serializeNBT()), player);
    }

    public void syncToTeam(ServerWorld world) {
        PenguinNetwork.sendToTeam(new SyncTeamDataPacket(serializeNBT()), world, teamUUID);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putString("UUID", teamUUID.toString());
        compound.put("Data", data);
        ListNBT list = new ListNBT();
        members.forEach(uuid -> list.add(StringNBT.valueOf(uuid.toString())));
        compound.put("Members", list);
        if (owner != null) {
            compound.putString("Owner", owner.toString());
        }

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound) {
        this.teamUUID = UUID.fromString(compound.getString("UUID"));
        this.data = compound.getCompound("Data");
        this.members = Sets.newHashSet();
        this.owner = compound.contains("Owner") ? UUID.fromString(compound.getString("Owner")) : null;
        ListNBT list = compound.getList("Members", 8);
        for (int i = 0; i < list.size(); i++) {
            members.add(UUID.fromString(list.getString(i)));
        }

        if (this.owner == null) {
            this.owner = members.stream().findFirst().orElse(null);
            MinecraftForge.EVENT_BUS.post(new TeamChangedOwnerEvent(teamUUID, owner));
        }
    }
}
