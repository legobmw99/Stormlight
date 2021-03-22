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

    private final Surge first;
    private final Surge second;

    Order(Surge fst, Surge snd) {
        this.first = fst;
        this.second = snd;

    }

    public Surge getFirst() {
        return first;
    }

    public Surge getSecond() {
        return second;
    }

    public static Order getOrNull(int index) {
        for (Order order : values()) {
            if (order.getIndex() == index) {
                return order;
            }
        }
        return null;
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
}
