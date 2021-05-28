package com.legobmw99.stormlight.modules.powers.data;

import com.legobmw99.stormlight.api.ISurgebindingData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SurgebindingDataProvider implements ICapabilitySerializable<CompoundNBT> {

    private final DefaultSurgebindingData data = new DefaultSurgebindingData();
    private final LazyOptional<ISurgebindingData> dataOptional = LazyOptional.of(() -> this.data);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return this.dataOptional.cast();
    }


    @Override
    public CompoundNBT serializeNBT() {
        if (SurgebindingCapability.PLAYER_CAP == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) SurgebindingCapability.PLAYER_CAP.writeNBT(this.data, null);
        }

    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (SurgebindingCapability.PLAYER_CAP != null) {
            SurgebindingCapability.PLAYER_CAP.readNBT(this.data, null, nbt);
        }
    }

    public void invalidate() {
        this.dataOptional.invalidate();
    }
}