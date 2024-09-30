package org.boschingmachine.goofyahhmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.server.command.EnumArgument;

public class GameRunnerCommand
{
    public enum CommandType
    {
        Start,
        Join,
        Leave,
    }

    public static void Register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("gamerunner").then(Commands.argument("enum", EnumArgument.enumArgument(CommandType.class)).executes(ctx ->
        {
            var cmdType = ctx.getArgument("enum", CommandType.class);

            switch (cmdType)
            {
                case Start ->
                {
                    ctx.getSource().sendSystemMessage(Component.literal("Start"));
                }
                case Join ->
                {
                    ctx.getSource().sendSystemMessage(Component.literal("Join"));
                }
                case Leave ->
                {
                    ctx.getSource().sendSystemMessage(Component.literal("Leave"));
                }
            }

            return 0;
        })));
    }
}
