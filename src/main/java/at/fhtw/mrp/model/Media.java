package at.fhtw.mrp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;




@Getter
@Setter
@AllArgsConstructor
public class Media {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    private int creatorId;
    private String title;
    private String description;
    private Integer releaseYear;
    private Integer ageRestriction;
    private List<Genre> genres;
    private MediaType type;

    //nur fuer Filme
    private Integer duration;

    public MediaType getMediaType() {
        return type;
    }
}