package org.boschingmachine.goofyahhmod.dimensions;

public record PacketSyncDimensionListChanges(Set<ResourceKey<Level>> newDimensions, Set<ResourceKey<Level>> removedDimensions) implements CustomPacketPayload
{
}
