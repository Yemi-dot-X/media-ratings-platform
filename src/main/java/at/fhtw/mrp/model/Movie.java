package at.fhtw.mrp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class Movie extends Media{
    private int duration;

    public Movie(
            int creatorId,
            int id,
            String title,
            String description,
            int releaseYear,
            int ageRestriction,
            List<Genre> genres,
            int duration
    ) {
        super(
                creatorId,
                id,
                title,
                description,
                releaseYear,
                ageRestriction,
                genres
        );

        this.duration = duration;
    }
}