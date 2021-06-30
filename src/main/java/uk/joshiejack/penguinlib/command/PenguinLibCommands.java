package uk.joshiejack.penguinlib.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.PenguinLib;

@Mod.EventBusSubscriber(modid = PenguinLib.MODID)
public class PenguinLibCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        if (PenguinLib.PenguinConfig.enableTeamCommands.get()) {
            event.getDispatcher().register(
                    LiteralArgumentBuilder.<CommandSource>literal("penguin")
                            .then(Commands.literal("team")
                                    .then(CreateTeamCommand.register())
                                    .then(InviteCommand.register())
                                    .then(ListInvitesCommand.register())
                                    .then(JoinTeamCommand.register())
                                    .then(LeaveTeamCommand.register())
                                    .then(RenameTeamCommand.register())
                                    .then(RejectTeamCommand.register()))
            );
        }
    }
}
