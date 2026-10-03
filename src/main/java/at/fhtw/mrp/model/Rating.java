package at.fhtw.mrp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Rating {
    private int id;
    private int userId;
    private int mediaId;

    @Setter
    private int stars;

    @Setter
    private String comments;

}
