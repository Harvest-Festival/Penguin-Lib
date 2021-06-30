package uk.joshiejack.penguinlib.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;

import java.util.UUID;

public class RejectTeamCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("reject")
                .then(Commands.argument("name", StringArgumentType.string())
                        .executes(ctx -> {
                            PenguinTeams teams = PenguinTeams.getTeamsFromContext(ctx);
                            CommandSource source = ctx.getSource();
                            UUID playerID = source.getPlayerOrException().getUUID();
                            String name = StringArgumentType.getString(ctx, "name");
                            PenguinTeam joining = teams.getTeamByName(name);

                            //If the team doesn't exist
                            if (joining == null) {
                                source.sendFailure(new TranslationTextComponent("command.penguinlib.team.reject.not_exist", name));
                                return 0;
                            }

                            if (!joining.isInvited(playerID)) {
                                source.sendFailure(new TranslationTextComponent("command.penguinlib.team.reject.not_invited", name));
                                return 0;
                            }

                            source.sendSuccess(new TranslationTextComponent("command.penguinlib.team.reject.success", name), false);
                            joining.clearInvite(playerID);
                            return 1;
                        }));
    }
}