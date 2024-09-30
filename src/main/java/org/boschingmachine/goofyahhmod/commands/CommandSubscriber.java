package org.boschingmachine.goofyahhmod.commands;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.boschingmachine.goofyahhmod.GoofyAhhMod;

@Mod.EventBusSubscriber(modid = GoofyAhhMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CommandSubscriber
{
    @SubscribeEvent
    public static void RegisterCommands(RegisterClientCommandsEvent event)
    {
        GameRunnerCommand.Register(event.getDispatcher());
    }
}
