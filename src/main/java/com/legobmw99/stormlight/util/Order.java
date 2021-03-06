package com.legobmw99.stormlight.util;

import java.util.Locale;

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
    BONDSMITHS(TENSION, ADHESION);


    // todo store three ideals - a blade, ideal one, ideal two
    private final Surge first;
    private final Surge second;

    Order(Surge fst, Surge snd) {
        this.first = fst;
        this.second = snd;

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
