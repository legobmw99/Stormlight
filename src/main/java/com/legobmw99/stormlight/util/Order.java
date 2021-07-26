package com.legobmw99.stormlight.util;

import java.util.Locale;

import static com.legobmw99.stormlight.util.Ideal.*;
import static com.legobmw99.stormlight.util.Surge.*;

public enum Order {
    WINDRUNNERS(ADHESION, GRAVITATION),
    SKYBREAKERS(GRAVITATION, DIVISION),
    DUSTBRINGERS(DIVISION, ABRASION),
    EDGEDANCERS(ABRASION, PROGRESSION),
    TRUTHWATCHERS(PROGRESSION, ILLUMINATION),
    LIGHTWEAVERS(ILLUMINATION, TRANSFORMATION),
    ELSECALLERS(TRANSFORMATION, TRANSPORTATION),
    WILLSHAPERS(TRANSPORTATION, COHESION),
    STONEWARDS(COHESION, TENSION),
    BONDSMITHS(TENSION, ADHESION, FIFTH, SECOND, THIRD);


    private final Surge first;
    private final Surge second;
    private final Ideal blade_gate;
    private final Ideal first_gate;
    private final Ideal second_gate;

    Order(Surge fst, Surge snd) {
        this(fst, snd, FIRST, FIRST, FIRST);
    }

    Order(Surge fst, Surge snd, Ideal blade, Ideal first, Ideal second) {
        this.first = fst;
        this.second = snd;
        this.blade_gate = blade;
        this.first_gate = first;
        this.second_gate = second;
    }

    public static Order getOrNull(int index) {
        for (Order order : values()) {
            if (order.getIndex() == index) {
                return order;
            }
        }
        return null;
    }

    public Surge getFirst() {
        return first;
    }

    public Surge getSecond() {
        return second;
    }

    public Ideal getFirstIdeal() {return first_gate;}

    public Ideal getSecondIdeal() {return second_gate;}

    public Ideal getBladeIdeal() {return blade_gate;}


    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String getSingularName() {
        String name = this.getName();
        return name.substring(0, name.length() - 1);
    }

    public int getIndex() {
        return ordinal();
    }

    public boolean hasSurge(Surge surge) {
        return first == surge || second == surge;
    }
}
