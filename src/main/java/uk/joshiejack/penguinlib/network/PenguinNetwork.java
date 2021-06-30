package uk.joshiejack.penguinlib.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;


@SuppressWarnings("WeakerAccess, unused")
public class PenguinNetwork {
    private static final PenguinNetwork INSTANCE = new PenguinNetwork(PenguinLib.MODID);
    private final SimpleChannel channel;
    private int id;

    private PenguinNetwork(String name) {
        Supplier<String> supplier = () -> "1";
        Predicate<String> predicate = (s) -> s.equals(supplier.get());
        channel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(name, "main"))
                .clientAcceptedVersions(predicate)
                .serverAcceptedVersions(predicate)
                .networkProtocolVersion(supplier)
                .simpleChannel();
    }

    public static <MSG extends PenguinPacket> void registerPacket(Class<MSG> type, NetworkDirection direction) {
        Optional<NetworkDirection> optional = Optional.of(direction);
        INSTANCE.channel.registerMessage(INSTANCE.id++, type, PenguinPacket::encode,
                (pb) -> {
                    MSG packet = null;
                    try {
                        packet = type.newInstance();
                        packet.decode(pb);
                    } catch (IllegalAccessException | InstantiationException ignored) {
                    }

                    return packet;
                }, PenguinPacket::handle, optional);
    }

    public static void sendToClient(PenguinPacket packet, @Nullable ServerPlayerEntity player) {
        if (player != null)
            INSTANCE.channel.sendTo(packet, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToTeam(PenguinPacket packet, ServerWorld world, UUID uuid) {
        PenguinTeam team = PenguinTeams.getTeamFromID(world, uuid);
        if (team != null) {
            team.members().stream()
                    .map(world::getPlayerByUUID)
                    .filter(player -> player instanceof ServerPlayerEntity)
                    .map(player -> (ServerPlayerEntity) player)
                    .forEach(player -> sendToClient(packet, player));
        }
    }

    public static void sendToServer(PenguinPacket packet) {
        INSTANCE.channel.sendToServer(packet);
    }

    public static void sendToEveryone(PenguinPacket packet) {
        INSTANCE.channel.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static void sendToDimension(PenguinPacket packet, RegistryKey<World> dimension) {
        INSTANCE.channel.send(PacketDistributor.DIMENSION.with(() -> dimension), packet);
    }

    public static void sendToAllAround(PenguinPacket packet, RegistryKey<World> dimension, double x, double y, double z, int distance) {
        INSTANCE.channel.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(x, y, z, distance, dimension)), packet);
    }

    public static void sendToNearby(PenguinPacket packet, World world, BlockPos pos) {
        if (world instanceof ServerWorld)
            ((ServerWorld) world).getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)
                    .forEach(player -> sendToClient(packet, player));
        else
            INSTANCE.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(pos.getX() >> 4, pos.getZ() >> 4)), packet);
    }

    public static void sendToNearby(PenguinPacket packet, Entity entity) {
        sendToNearby(packet, entity.level, entity.blockPosition());
    }

    public static void sendToNearby(PenguinPacket packet, TileEntity tile) {
        sendToNearby(packet, tile.getLevel(), tile.getBlockPos());
    }
}
