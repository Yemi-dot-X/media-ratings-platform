package at.fhtw.mrp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class Movie extends Media{
    private int duration;

    @JsonCreator
    public Movie(
            @JsonProperty("id") int id,
            @JsonProperty("creatorId") int creatorId,

            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("releaseYear") int releaseYear,
            @JsonProperty("ageRestriction") int ageRestriction,
            @JsonProperty("genres") List<Genre> genres,
            @JsonProperty("duration") int duration
    ) {
        super(

                id,
                creatorId,
                title,
                description,
                releaseYear,
                ageRestriction,
                genres
        );

        this.duration = duration;
    }
}