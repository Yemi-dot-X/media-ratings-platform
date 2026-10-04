package at.fhtw.mrp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public abstract class Media {

    private int creatorId;
    private final int id;
    private String title;
    private final String description;
    private final int releaseYear;
    private final int ageRestriction;
    private List<Genre> genres;
}