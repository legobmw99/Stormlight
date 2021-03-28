package com.legobmw99.stormlight.util;

import java.util.Locale;

public enum Ideal {

    TRAITOR,
    UNINVESTED,
    FIRST,
    SECOND,
    THIRD,
    FOURTH,
    FIFTH;

    public static Ideal get(int index) {
        for (Ideal order : values()) {
            if (order.getIndex() == index) {
                return order;
            }
        }
        throw new IllegalArgumentException("Bad ideal index");
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public int getIndex() {
        return ordinal() - 1;
    }

    public Ideal progressIdeal() {
        int i = getIndex() + 1;
        if (i > 5) {
            i = 5;
        }
        return get(i);
    }

    public Ideal regressIdeal() {
        int i = getIndex() - 1;
        if (i < -1) {
            i = -1;
        }
        return get(i);
    }
}
