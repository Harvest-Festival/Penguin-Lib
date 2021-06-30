package uk.joshiejack.penguinlib.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;

import java.util.UUID;

public class CreateTeamCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("create")
                .then(Commands.argument("name", StringArgumentType.string())
                .executes(ctx -> {
                    PenguinTeams teams = PenguinTeams.getTeamsFromContext(ctx);
                    PenguinTeam current = PenguinTeams.getTeamFromContext(ctx);
                    CommandSource source = ctx.getSource();
                    UUID playerID = source.getPlayerOrException().getUUID();
                    //If the player is in a team already, they cannot create one
                    if (!current.getID().equals(playerID)) {
                        source.sendFailure(new TranslationTextComponent("command.penguinlib.team.create.must_leave",
                                StringHelper.withClickableCommand("/penguin team leave", "command.penguinlib.team.leave.tooltip")));
                        return 0;
                    }

                    //If the team name is already in use then error
                    String name = StringArgumentType.getString(ctx, "name");
                    if (teams.nameExists(name)) {
                        source.sendFailure(new TranslationTextComponent("command.penguinlib.team.create.name_in_use", name));
                        return 0;
                    }

                    teams.changeTeam(ctx, UUID.randomUUID(), (pt) -> pt.setName(name));
                    source.sendSuccess(new TranslationTextComponent("command.penguinlib.team.create.success", name), false);
                    return 1;
                }));
    }
}
