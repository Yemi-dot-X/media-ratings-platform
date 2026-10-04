package at.fhtw.mrp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class Series extends Media {
    public Series(
        int creatorId,
        int id,
        String title,
        String description,
        int releaseYear,
        int ageRestriction,
        List<Genre> genres

        ) {
    super(creatorId, id, title, description, releaseYear, ageRestriction, genres);
    }
}
