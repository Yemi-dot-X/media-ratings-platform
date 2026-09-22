package at.fhtw.mrp.model;

public class Game extends Media {
    public Game(
            int id,
            String title,
            String description,
            int releaseYear,
            int ageRestriction) {
        super(id, title, description, releaseYear, ageRestriction);
    }
}
