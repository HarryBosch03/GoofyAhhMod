package org.boschingmachine.goofyahhmod.game_management;

import com.mojang.brigadier.context.CommandContext;
import mcjty.rftoolsdim.dimension.data.DimensionCreator;
import net.minecraft.client.gui.components.AccessibilityOnboardingTextWidget;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class GameRunner
{
    public ServerLevel level;

    public GameRunner(ServerLevel level)
    {
        this.level = level;
    }

    public void Join(ServerPlayer player)
    {
        var spawnPos = level.getSharedSpawnPos();
        var spawnRot = level.getSharedSpawnAngle();
        player.teleportTo(level, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), spawnRot, 0f);
    }

    public void Leave(ServerPlayer player)
    {
        var overworld = level.getServer().getLevel(Level.OVERWORLD);
        var spawnPos = overworld.getSharedSpawnPos();
        var spawnRot = overworld.getSharedSpawnAngle();
        player.teleportTo(overworld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), spawnRot, 0f);
    }
}
