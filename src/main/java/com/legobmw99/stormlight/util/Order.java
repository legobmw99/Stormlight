package com.legobmw99.stormlight.util;

import java.util.Locale;

public enum Order {
    WINDRUNNERS,
    SKYBREAKERS,
    DUSTBRINGERS,
    EDGEDANCERS,
    TRUTHWATCHERS,
    LIGHTWEAVERS,
    ELSECALLERS,
    WILLSHAPERS,
    STONEWARDS,
    BONDSMITHS;


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
