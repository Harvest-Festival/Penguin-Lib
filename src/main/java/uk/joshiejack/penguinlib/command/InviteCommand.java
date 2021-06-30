package uk.joshiejack.penguinlib.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;

public class InviteCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("invite")
                .then(Commands.argument("player_name", StringArgumentType.string())
                        .executes(ctx -> {
                            PenguinTeam current = PenguinTeams.getTeamFromContext(ctx);
                            if (current.getID().equals(ctx.getSource().getPlayerOrException().getUUID())) {
                                ctx.getSource().sendSuccess(new TranslationTextComponent("command.penguinlib.team.invite.personal"), false);
                                return 0;
                            }

                            String playerName = StringArgumentType.getString(ctx, "player_name");
                            ServerPlayerEntity player = ctx.getSource().getLevel().getServer().getPlayerList().getPlayerByName(playerName);
                            if (player == null) {
                                ctx.getSource().sendSuccess(new TranslationTextComponent("command.penguinlib.team.invite.failure", playerName), false);
                                return 0;
                            } else {
                                PenguinTeams.get(ctx.getSource().getLevel()).getTeam(ctx.getSource().getPlayerOrException().getUUID()).invite(ctx.getSource().getLevel(), player.getUUID());
                                ctx.getSource().sendSuccess(new TranslationTextComponent("command.penguinlib.team.invite.success", playerName), false);
                                player.createCommandSourceStack().sendSuccess(new TranslationTextComponent("command.penguinlib.team.invite.message",
                                        current.getName(), StringHelper.withClickableCommand(TextFormatting.GREEN, "/penguin team join " + current.getName(), "command.penguinlib.team.invite.tooltip")), false);
                                return 1;
                            }
                        }));
    }
}