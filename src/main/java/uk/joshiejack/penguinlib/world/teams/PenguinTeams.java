package uk.joshiejack.penguinlib.world.teams;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.events.TeamChangedEvent;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.ChangeTeamPacket;
import uk.joshiejack.penguinlib.network.packet.SyncPlayerStatusesPacket;
import uk.joshiejack.penguinlib.network.packet.SyncTeamMembersPacket;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID)
public class PenguinTeams extends WorldSavedData {
    private static final String DATA_NAME = "penguin_teams";
    private final Map<UUID, UUID> memberOf = new HashMap<>(); //Player ID > Team ID
    private final Map<UUID, PenguinTeam> teams = new HashMap<>(); // Team ID > Data

    public PenguinTeams() { super(DATA_NAME); }
    public PenguinTeams(String name) {
        super(name);
    }

    public static PenguinTeams get(ServerWorld world) {
        return world.getDataStorage().computeIfAbsent(PenguinTeams::new, DATA_NAME);
        /*
        if (instance == null) {
            instance = new PenguinTeams(DATA_NAME);
            world.setData(DATA_NAME, instance);
            instance.markDirty(); //Save the file
        }

        return instance; */
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            PenguinTeams.getTeamForPlayer(player).syncToPlayer(player); //Sync the info about this players team to them
            PenguinNetwork.sendToClient(new SyncPlayerStatusesPacket(player.getPersistentData().getCompound("PenguinStatuses")), player);
            PenguinNetwork.sendToClient(new SyncTeamMembersPacket(PenguinTeams.get((ServerWorld) player.level).memberOf), player);
        }
    }

    public int getMemberCount(UUID owner_id) {
        UUID team = memberOf.get(owner_id);
        return teams.get(team).members().size();
    }

    public void changeTeam(ServerWorld world, UUID player, UUID newUUID) {
        UUID oldUUID = memberOf.getOrDefault(player, player);
        memberOf.put(player, newUUID);
        PenguinTeam oldTeam = teams.get(oldUUID);
        if (oldTeam != null) {
            oldTeam.members().remove(player);
            oldTeam.onChanged(world);
        }

        if (!teams.containsKey(newUUID)) {
            teams.put(newUUID, new PenguinTeam(newUUID));
        }

        PenguinTeam newTeam = teams.get(newUUID);
        newTeam.members().add(player);
        newTeam.onChanged(world);
        MinecraftForge.EVENT_BUS.post(new TeamChangedEvent(world, player, oldUUID, newUUID));
        PenguinNetwork.sendToEveryone(new ChangeTeamPacket(player, oldUUID, newUUID));
        setDirty();
    }

    public CompoundNBT getTeamData(UUID team) {
        return teams.get(team).getData();
    }

    public Collection<UUID> getTeamMembers(UUID team) {
        return teams.get(team).members();
    }

    public static PenguinTeam getTeamFromID(ServerWorld world, UUID team) {
        return get(world).teams.get(team);
    }

    public static PenguinTeam getTeamForPlayer(ServerWorld world, UUID uuid) {
        PenguinTeams data = get(world); //Load the serverdata
        if (!data.memberOf.containsKey(uuid)) {
            //data.teams.put(uuid, new PenguinTeam(uuid);
            //data.memberOf.put(uuid, uuid);
            data.changeTeam(world, uuid, uuid);
            data.setDirty();
        }

        return data.teams.get(data.memberOf.get(uuid));
    }

    public static PenguinTeam getTeamForPlayer(PlayerEntity player) {
        if (player.level.isClientSide) return PenguinTeamsClient.getInstance(); //Client data
        return getTeamForPlayer((ServerWorld) player.level, player.getUUID());
    }

    public UUID getTeamUUIDForPlayer(PlayerEntity player) {
        return getTeamForPlayer(player).getID();
    }

    @Override
    public void load(@Nonnull CompoundNBT nbt) {
        ListNBT data = nbt.getList("Teams", 10);
        for (int i = 0; i < data.size(); i++) {
            CompoundNBT tag = data.getCompound(i);
            PenguinTeam team = new PenguinTeam(tag);
            teams.put(team.getID(), team);
            team.members().forEach(member -> memberOf.put(member, team.getID())); //Add the quick reference for members
        }
    }

    @Override
    public CompoundNBT save(@Nonnull CompoundNBT compound) {
        ListNBT data = new ListNBT();
        for (Map.Entry<UUID, PenguinTeam> entry: teams.entrySet()) {
            data.add(entry.getValue().serializeNBT());
        }

        compound.put("Teams", data);

        return compound;
    }
}
