package uk.joshiejack.penguinlib.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.UsernameCache;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;

public class ListInvitesCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("invite_list")
                .executes(ctx -> {
                    boolean success = false;
                    for (PenguinTeam team: PenguinTeams.get(ctx.getSource().getLevel()).teams()) {
                        if (team.isInvited(ctx.getSource().getPlayerOrException().getUUID())) {
                            ctx.getSource().getPlayerOrException().createCommandSourceStack().sendSuccess(new TranslationTextComponent("command.penguinlib.team.invite.message",
                                    team.getName(), StringHelper.withClickableCommand(TextFormatting.GREEN, "/penguin team join %s", team.getName())), false);
                            success = true;
                        }
                    }

                    if (!success) {
                        ctx.getSource().sendFailure(new TranslationTextComponent("command.penguinlib.team.invite.none"));
                    }

                    return success ? 1 : 0;
                });
    }
}