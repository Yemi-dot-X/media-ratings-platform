package at.fhtw.mrp.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MediaType {
    MOVIE,
    SERIES,
    GAME;

    @JsonCreator
    public static MediaType from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown media type: " + value);
        }
    }
}



