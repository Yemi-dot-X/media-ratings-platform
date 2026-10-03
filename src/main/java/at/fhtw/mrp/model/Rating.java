package at.fhtw.mrp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Rating {
    private int id;
    private User user;
    private Media media;
    private int stars;
    private String comments;

}
