package at.fhtw.mrp.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Genre {
    ACTION,
    DRAMA,
    THRILLER,
    COMEDY,
    HORROR,
    DOCUMENTARY,
    ROMANCE,
    FANTASY,
    SCIENCE_FICTION,
    ANIMATION;

    // Akzeptiert "ACTION", "action", "science-fiction" und "sci-fi".

    @JsonCreator
    public static Genre from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String name = value.trim().toUpperCase().replace('-', '_').replace(' ', '_');
        if (name.equals("SCI_FI")) {
            name = "SCIENCE_FICTION";
        }
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown genre: " + value);
        }
    }

}
