package com.legobmw99.stormlight.util;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum Gemstone implements IStringSerializable {
    DIAMOND(5),
    EMERALD(3),
    QUARTZ(1);

    private final int modifier;

    Gemstone(int mod) {
        this.modifier = mod;
    }

    public int getModifier() {
        return modifier;
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public int getIndex() {
        return ordinal();
    }

    @Override
    public String getSerializedName() {
        return this.getName();
    }
}