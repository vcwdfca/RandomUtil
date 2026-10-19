package com.vcwdfca.randomutil.feature;

public interface Feature {

    String id();

    default boolean enabled() {
        return true;
    }

    default void init() {}

    default void clientInit() {}
}
