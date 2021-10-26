package com.legobmw99.stormlight.modules.powers.data;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.api.ISurgebindingData;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class SurgebindingCapability {

    public static final Capability<ISurgebindingData> PLAYER_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final ResourceLocation IDENTIFIER = new ResourceLocation(Stormlight.MODID, "surgebinding_data");


    public static void registerCapability(final RegisterCapabilitiesEvent event) {
        event.register(ISurgebindingData.class);
    }

}
