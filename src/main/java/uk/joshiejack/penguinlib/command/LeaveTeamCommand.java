package uk.joshiejack.penguinlib.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;

import java.util.UUID;

public class LeaveTeamCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("leave")
                .executes(ctx -> {
                    PenguinTeams teams = PenguinTeams.getTeamsFromContext(ctx);
                    PenguinTeam current = PenguinTeams.getTeamFromContext(ctx);
                    CommandSource source = ctx.getSource();
                    UUID playerID = source.getPlayerOrException().getUUID();
                    //If this is a single player team you cannot leave
                    if (current.getID().equals(playerID)) {
                        source.sendFailure(new TranslationTextComponent("command.penguinlib.team.leave.cannot"));
                        return 0;
                    }

                    teams.changeTeam(ctx, playerID, (pt) -> {});
                    source.sendSuccess(new TranslationTextComponent("command.penguinlib.team.leave.success"), false);
                    return 1;
                });
    }
}