package uk.joshiejack.penguinlib.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;

import java.util.UUID;

public class RenameTeamCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("rename")
                .then(Commands.argument("name", StringArgumentType.string())
                .executes(ctx -> {
                    PenguinTeams teams = PenguinTeams.getTeamsFromContext(ctx);
                    PenguinTeam current = PenguinTeams.getTeamFromContext(ctx);
                    CommandSource source = ctx.getSource();
                    UUID playerID = source.getPlayerOrException().getUUID();
                    //If the player is in a team already, they cannot create one
                    if (!playerID.equals(current.getOwner())) {
                        source.sendFailure(new TranslationTextComponent("command.penguinlib.team.rename.not_owner"));
                        return 0;
                    }

                    //If the team name is already in use then error
                    String name = StringArgumentType.getString(ctx, "name");
                    if (teams.nameExists(name)) {
                        source.sendFailure(new TranslationTextComponent("command.penguinlib.team.rename.name_in_use"));
                        return 0;
                    }

                    teams.changeTeam(ctx, current.getID(), (pt) -> pt.setName(name));
                    source.sendSuccess(new TranslationTextComponent("command.penguinlib.team.rename.success"), false);
                    return 1;
                }));
    }
}
