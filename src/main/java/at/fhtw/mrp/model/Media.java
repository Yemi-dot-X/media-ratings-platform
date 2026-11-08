package at.fhtw.mrp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;




@Getter
@Setter
@NoArgsConstructor
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
    private MediaType mediaType;

    //nur fuer Filme
    private Integer duration;

}