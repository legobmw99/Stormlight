package com.legobmw99.stormlight.datagen;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.combat.CombatSetup;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.util.Gemstone;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class Languages extends LanguageProvider {


    public Languages(DataGenerator gen) {
        super(gen, Stormlight.MODID, "en_us");
    }

    private static String toTitleCase(String in) {
        return in.substring(0, 1).toUpperCase() + in.substring(1);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.stormlight", "Stormlight");


        for (Order o : Order.values()) {
            add(CombatSetup.SHARDBLADES.get(o.getIndex()).get(), "Shardblade of the " + toTitleCase(o.getName()));
            add("orders." + o.getName(), toTitleCase(o.getName()));
        }

        for (Gemstone g : Gemstone.values()) {
            add(WorldSetup.DUN_SPHERES.get(g.getIndex()).get(), "Dun " + toTitleCase(g.getName()) + " Sphere");
            add(WorldSetup.INFUSED_SPHERES.get(g.getIndex()).get(), toTitleCase(g.getName()) + " Sphere");
            add("gemstone." + g.getName(), toTitleCase(g.getName()));

        }

        add("item.stormlight.shardblade.lore", "This sword seems to glow with strange light");

        add("effect.stormlight.stormlight", "Stormlight");

        add("key.categories.stormlight", "Stormlight");
        add("key.blade", "Summon Shardblade");
        add("key.firstSurge", "First Surge");
        add("key.secondSurge", "Second Surge");


    }

    @Override
    public String getName() {
        return "Stormlight Language";
    }
}