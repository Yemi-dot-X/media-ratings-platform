package at.fhtw.mrp.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;



@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include =  JsonTypeInfo.As.PROPERTY,
        property = "type"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = Movie.class, name = "MOVIE"),
        @JsonSubTypes.Type(value = Series.class, name = "SERIES"),
        @JsonSubTypes.Type(value = Game.class, name = "GAME")
})

@Getter
@Setter
@AllArgsConstructor
public abstract class Media {

    private int creatorId;
    private final int id;
    private String title;
    private String description;
    private int releaseYear;
    private int ageRestriction;
    private List<Genre> genres;
}