package at.fhtw.mrp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Game extends Media {
    public Game(
            @JsonProperty("creatorId") int creatorId,
            @JsonProperty("id") int id,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("releaseYear") int releaseYear,
            @JsonProperty("ageRestriction") int ageRestriction,
            @JsonProperty("genres") List<Genre> genres
    ) {
        super(creatorId, id, title, description,
                releaseYear, ageRestriction, genres);
    }
}
