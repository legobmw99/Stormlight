package com.legobmw99.stormlight.modules.combat;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.combat.item.ShardbladeItem;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class CombatSetup {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Stormlight.MODID);
    public static final List<RegistryObject<ShardbladeItem>> SHARDBLADES = new ArrayList<>();

    static {
        for (Order order : Order.values()) {
            String name = order.getSingularName();
            SHARDBLADES.add(ITEMS.register(name + "_shardblade", () -> new ShardbladeItem(order)));
        }
    }

    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientInit(final FMLClientSetupEvent e) {
        e.enqueueWork(() -> {
            for (RegistryObject<ShardbladeItem> shardblade : CombatSetup.SHARDBLADES) {
                ItemProperties.register(shardblade.get(), new ResourceLocation(Stormlight.MODID, "shielding"),
                                        (itemStack, clientWorld, player, seed) -> player != null && player.isUsingItem() && player.getUseItem() == itemStack ? 1.1F : 0.0F);
            }
        });
    }
}
