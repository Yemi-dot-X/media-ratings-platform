package at.fhtw.mrp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Game extends Media {
    public Game(
            int id,
            String title,
            String description,
            int releaseYear,
            int ageRestriction,
            List<Genre> genres
    ) {
        super(id, title, description, releaseYear, ageRestriction, genres);
    }
}
