package com.example.demo;

public enum DietaryFlag {
    VEGAN('V'),
    VEGETARIAN('T'),
    GLUTEN_FREE('G'),
    NUT_FREE('N'),
    SPICY('S');

    private final char code;

    DietaryFlag(char code) { this.code = code; }

    public char getCode() { return code; }

    public static DietaryFlag fromCode(char code) {
        for (DietaryFlag f : values()) {
            if (f.code == code) return f;
        }
        throw new IllegalArgumentException("Unknown dietary code: " + code);
    }
}
