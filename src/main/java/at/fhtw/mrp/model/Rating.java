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
    private int score;
    private String comment;
    private int stars;


}
