package com.legobmw99.stormlight.modules.powers.data;

import com.legobmw99.stormlight.api.ISurgebindingData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SurgebindingDataProvider implements ICapabilitySerializable<CompoundTag> {

    private final DefaultSurgebindingData data = new DefaultSurgebindingData();
    private final LazyOptional<ISurgebindingData> dataOptional = LazyOptional.of(() -> this.data);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return SurgebindingCapability.PLAYER_CAP.orEmpty(cap, this.dataOptional.cast());
    }


    @Override
    public CompoundTag serializeNBT() {
        if (SurgebindingCapability.PLAYER_CAP == null) {
            return new CompoundTag();
        } else {
            return this.data.save();
        }

    }

    @Override
    public void deserializeNBT(CompoundTag data) {
        if (SurgebindingCapability.PLAYER_CAP != null) {
            this.data.load(data);
        }
    }

    public void invalidate() {
        this.dataOptional.invalidate();
    }
}