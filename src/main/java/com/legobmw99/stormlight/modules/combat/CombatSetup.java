package com.legobmw99.stormlight.modules.combat;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.combat.item.ShardbladeItem;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
}
