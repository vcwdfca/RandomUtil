package com.vcwdfca.randomutil.feature.thaum;

import java.util.Locale;

public record Chapter(String key) {

    public static final Chapter ALL = new Chapter("*");

    public static Chapter of(String key) {
        if (key == null || key.isEmpty() || "*".equals(key) || "all".equalsIgnoreCase(key)) {
            return ALL;
        }

        return new Chapter(key.toUpperCase(Locale.ROOT));
    }

    public boolean isAll() {
        return "*".equals(key);
    }
}
