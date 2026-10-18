package com.vcwdfca.randomutil.feature;

public interface Feature {

    String id();

    default void init() {}

    default void clientInit() {}
}
