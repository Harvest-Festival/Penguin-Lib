package uk.joshiejack.penguinlib.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.UsernameCache;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JoinTeamCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("join")
                .then(Commands.argument("name", StringArgumentType.string())
                        .executes(ctx -> {
                            PenguinTeams teams = PenguinTeams.getTeamsFromContext(ctx);
                            PenguinTeam current = PenguinTeams.getTeamFromContext(ctx);
                            CommandSource source = ctx.getSource();
                            UUID playerID = source.getPlayerOrException().getUUID();
                            String name = StringArgumentType.getString(ctx, "name");
                            PenguinTeam joining = teams.getTeamByName(name);
                            //If the player is in a team already, they cannot join one
                            if (!current.getID().equals(playerID)) {
                                source.sendFailure(new TranslationTextComponent("command.penguinlib.team.join.must_leave",
                                        StringHelper.withClickableCommand("/penguin team leave", "command.penguinlib.team.join.must_leave.tooltip")));
                                return 0;
                            }

                            //If the team doesn't exist
                            if (joining == null) {
                                source.sendFailure(new TranslationTextComponent("command.penguinlib.team.join.not_exist", name));
                                return 0;
                            }

                            if (joining.getOwner() != null && !joining.isInvited(playerID)) {
                                source.sendFailure(new TranslationTextComponent("command.penguinlib.team.join.not_invited", name, UsernameCache.getLastKnownUsername(joining.getOwner())));
                                return 0;
                            }

                            //If the owner is null or the player is invited they can join
                            if (joining.getOwner() == null) source.sendSuccess(new TranslationTextComponent("command.penguinlib.team.join.success.owner", name), false);
                            else source.sendSuccess(new TranslationTextComponent("command.penguinlib.team.join.success.member", name), false);
                            teams.changeTeam(ctx, joining.getID(), (pt) -> {});
                            return 1;
                        }));
    }
}