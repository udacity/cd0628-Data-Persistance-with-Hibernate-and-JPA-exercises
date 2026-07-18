package com.example.demo;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.EnumSet;
import java.util.Set;

/**
 * Converter that packs a Set<DietaryFlag> into a compact single-character string.
 * Demonstrates @Converter for turning application-side types into database-side
 * representations that don't have a native mapping.
 */
@Converter
public class DietaryFlagsConverter implements AttributeConverter<Set<DietaryFlag>, String> {

    @Override
    public String convertToDatabaseColumn(Set<DietaryFlag> flags) {
        if (flags == null || flags.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (DietaryFlag f : flags) sb.append(f.getCode());
        return sb.toString();
    }

    @Override
    public Set<DietaryFlag> convertToEntityAttribute(String db) {
        Set<DietaryFlag> flags = EnumSet.noneOf(DietaryFlag.class);
        if (db == null) return flags;
        for (char c : db.toCharArray()) {
            flags.add(DietaryFlag.fromCode(c));
        }
        return flags;
    }
}
