package at.fhtw.mrp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;




@Getter
@Setter
@AllArgsConstructor
public abstract class Media {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    private int creatorId;
    private String title;
    private String description;
    private int releaseYear;
    private int ageRestriction;
    private List<Genre> genres;

    //nur fuer Filme
    private Integer duration;
}