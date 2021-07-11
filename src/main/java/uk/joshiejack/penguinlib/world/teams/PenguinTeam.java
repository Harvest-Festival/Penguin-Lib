package uk.joshiejack.penguinlib.world.teams;

import com.google.common.collect.Sets;
import joptsimple.internal.Strings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.penguinlib.events.TeamChangedOwnerEvent;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.SyncTeamDataPacket;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class PenguinTeam implements INBTSerializable<CompoundNBT> {
    private boolean isClient;
    private CompoundNBT data;
    private Set<UUID> members;
    private Set<UUID> invited;
    private UUID teamUUID;
    private UUID owner;
    private String name = Strings.EMPTY;

    public PenguinTeam(UUID uuid) {
        this.teamUUID = uuid;
        this.members = new HashSet<>();
        this.invited = new HashSet<>();
        this.data = new CompoundNBT();
    }

    public void setClient() {
        this.isClient = true;
    }

    public boolean isClient() {
        return isClient;
    }

    public void invite(ServerWorld level, UUID uuid) {
        invited.add(uuid);
    }


    public void clearInvite(UUID playerID) {
        invited.remove(playerID);
    }

    public boolean isInvited(UUID uuid) {
        return invited.contains(uuid);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putString("UUID", teamUUID.toString());
        compound.putString("Name", name == null ? teamUUID.toString() : name);
        compound.put("Data", data);
        ListNBT list = new ListNBT();
        members.forEach(uuid -> list.add(StringNBT.valueOf(uuid.toString())));
        compound.put("Members", list);

        ListNBT invitedList = new ListNBT();
        invited.forEach(uuid -> invitedList.add(StringNBT.valueOf(uuid.toString())));
        compound.put("Invited", invitedList);
        if (owner != null) {
            compound.putString("Owner", owner.toString());
        }

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound) {
        teamUUID = UUID.fromString(compound.getString("UUID"));
        name = compound.contains("Name") ? compound.getString("Name") : teamUUID.toString();
        data = compound.getCompound("Data");
        members = new HashSet<>();
        invited = new HashSet<>();
        owner = compound.contains("Owner") ? UUID.fromString(compound.getString("Owner")) : null;
        ListNBT list = compound.getList("Members", 8);
        for (int i = 0; i < list.size(); i++) {
            members.add(UUID.fromString(list.getString(i)));
        }

        compound.getList("Invited", 8)
                .forEach(nbt -> invited.add(UUID.fromString(nbt.getAsString())));

        if (owner == null) {
            owner = members.stream().findFirst().orElse(null);
            MinecraftForge.EVENT_BUS.post(new TeamChangedOwnerEvent(teamUUID, owner));
        }
    }
}
