package org.boschingmachine.goofyahhmod.dimensions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.function.BiFunction;

public class DimensionTool {
    public Mod mod;

    private static DimensionTool instance;

    public static DimensionTool get() {
        return instance;
    }

    public DimensionTool() {
        instance = this;
    }

    public static ServerLevel createDimension(MinecraftServer server, Map<ResourceKey<Level>, ServerLevel> map, ResourceKey<Level> worldKey, final BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory)
    {
        var overworld = server.getLevel(Level.OVERWORLD);

        var dimensionKey = ResourceKey.create(Registries.LEVEL_STEM, worldKey.location());
        var dimension = dimensionFactory.apply(server, dimensionKey);

        var chunkProgressListener = server.progressListenerFactory.create(11);
        var executor = server.executor;
        var storageSource = server.storageSource;
        var worldData = server.getWorldData();
        var worldGenSettings = worldData.worldGenOptions();
        var derivedLevelData = new DerivedLevelData(worldData, worldData.overworldData());

        var registries = server.registries();
        var composite = (RegistryAccess.ImmutableRegistryAccess)registries.compositeAccess();

        var regmap = (Map<ResourceKey<? extends Registry<?>>, Registry<?>>)new HashMap<>(composite.registries);
        ResourceKey<? extends Registry<?>> key = ResourceKey.create(ResourceKey.createRegistryKey(new ResourceLocation("root")), new ResourceLocation("dimension"));
        var oldRegistry = (MappedRegistry<LevelStem>) regmap.get(key);
        var oldLifecycle = oldRegistry.registryLifecycle();

        final var newRegistry = new MappedRegistry<>(Registries.LEVEL_STEM, oldLifecycle, false);
        for (var entry : oldRegistry.entrySet())
        {
            final var oldKey = entry.getKey();
            final var oldLevelKey = ResourceKey.create(Registries.DIMENSION, oldKey.location());
            final var dim = entry.getValue();
            if (dim != null && oldLevelKey != worldKey)
            {
                Registry.register(newRegistry, oldKey, dim);
            }
        }
        Registry.register(newRegistry, dimensionKey, dimension);
        regmap.replace(key, newRegistry);
        var newmap = (Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>>) regmap;
        composite.registries = newmap;

        var newWorld = new ServerLevel
        (
            server,
            executor,
            storageSource,
            derivedLevelData,
            worldKey,
            dimension,
            chunkProgressListener,
            false,
            BiomeManager.obfuscateSeed(worldGenSettings.seed()),
            ImmutableList.of(),
            false,
            null
        );

        map.put(worldKey, newWorld);
        server.markWorldsDirty();

        MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(newWorld));

        PacketSyncDimensionListChanges.updateClientDimensionLists(ImmutableSet.of(worldKey), ImmutableSet.of());

        return newWorld;
    }
}
