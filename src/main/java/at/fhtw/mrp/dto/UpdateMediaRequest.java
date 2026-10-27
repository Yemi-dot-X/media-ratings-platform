//data transfer object
// is a software design pattern used in object-oriented programming
// to transfer data between different processes,
package at.fhtw.mrp.dto;

import at.fhtw.mrp.model.Genre;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateMediaRequest {
    private String title;
    private String description;
    private List<Genre> genres;
    private Integer releaseYear;
    private Integer ageRestriction;
    private Integer duration;
}
