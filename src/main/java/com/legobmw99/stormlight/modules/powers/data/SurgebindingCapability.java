package com.legobmw99.stormlight.modules.powers.data;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.api.ISurgebindingData;
import com.legobmw99.stormlight.util.Ideal;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class SurgebindingCapability {

    @CapabilityInject(ISurgebindingData.class)
    public static final Capability<ISurgebindingData> PLAYER_CAP = null;

    public static final ResourceLocation IDENTIFIER = new ResourceLocation(Stormlight.MODID, "surgebinding_data");


    public static void register() {
        CapabilityManager.INSTANCE.register(ISurgebindingData.class, new Storage(), DefaultSurgebindingData::new);
    }

    public static class Storage implements Capability.IStorage<ISurgebindingData> {

        @Override
        public INBT writeNBT(Capability<ISurgebindingData> capability, ISurgebindingData data, Direction side) {
            CompoundNBT nbt = new CompoundNBT();

            nbt.putInt("order", data.getOrder() != null ? data.getOrder().getIndex() : -1);
            nbt.putInt("ideal", data.getIdeal().getIndex());
            nbt.put("blade", data.getBlade().serializeNBT());

            if (data.getSprenID() != null) {
                nbt.putUUID("sprenID", data.getSprenID());
            }
            return nbt;
        }

        @Override
        public void readNBT(Capability<ISurgebindingData> capability, ISurgebindingData data, Direction side, INBT nbt) {
            CompoundNBT surgebinding_data = (CompoundNBT) nbt;
            data.setOrder(Order.getOrNull(surgebinding_data.getInt("order")));
            data.setIdeal(Ideal.get(surgebinding_data.getInt("ideal")));
            data.storeBlade(ItemStack.of(surgebinding_data.getCompound("blade")));

            if (surgebinding_data.contains("sprenID")) {
                data.setSprenID(surgebinding_data.getUUID("sprenID"));
            }
        }

    }
}
