package com.legobmw99.stormlight.datagen;

import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.combat.CombatSetup;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.util.Gemstone;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class Languages extends LanguageProvider {


    public Languages(PackOutput gen) {
        super(gen, Stormlight.MODID, "en_us");
    }

    private static String toTitleCase(String in) {
        return in.substring(0, 1).toUpperCase() + in.substring(1);
    }

    @Override
    protected void addTranslations() {
        add("tabs.stormlight.main_tab", "Stormlight");


        for (Order o : Order.values()) {
            add(CombatSetup.SHARDBLADES.get(o.getIndex()).get(), "Shardblade of the " + toTitleCase(o.getName()));
            add("orders." + o.getName(), toTitleCase(o.getName()));
        }

        for (Gemstone g : Gemstone.values()) {
            add(WorldSetup.DUN_SPHERES.get(g.getIndex()).get(), "Dun " + toTitleCase(g.getName()) + " Sphere");
            add(WorldSetup.INFUSED_SPHERES.get(g.getIndex()).get(), toTitleCase(g.getName()) + " Sphere");
            add("gemstone." + g.getName(), toTitleCase(g.getName()));

        }

        add(WorldSetup.ADHESION_BLOCK.get(), "Adhesion");

        add(WorldSetup.SPREN_SPAWN_EGG.get(), "Spren Spawn Egg");
        add("entity.stormlight.spren", "Spren");

        add("item.stormlight.shardblade.lore", "This sword seems to glow with strange light");

        add("effect.stormlight.stormlight", "Stormlight");
        add("effect.stormlight.sticking", "Sticking");
        add("effect.stormlight.slicking", "Slicking");
        add("effect.stormlight.cohesion", "Cohesion");
        add("effect.stormlight.tension", "Tension");
        add("effect.stormlight.gravitation", "Gravitation");

        add("surge.cohesion.stoneshaping", "Stoneshaping");
        add("surge.cohesion.soulcasting", "Soulcasting");


        add("commands.stormlight.getorder", "%s has order %s");
        add("commands.stormlight.setorder", "%s set to order %s");
        add("commands.stormlight.getideal", "%s has ideal %s");
        add("commands.stormlight.setideal", "%s set to ideal %s");
        add("commands.stormlight.unrecognized_order", "Unrecognized order %s");
        add("commands.stormlight.unrecognized_ideal", "Unrecognized ideal %s");


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