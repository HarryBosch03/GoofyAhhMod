package org.boschingmachine.goofyahhmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import org.boschingmachine.goofyahhmod.game_management.GameRunner;

public class GameStartCommand
{
    public static void Register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("gamestart").executes(ctx ->
        {
            return 0;
        }));
    }
}
