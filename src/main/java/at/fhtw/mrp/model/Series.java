package at.fhtw.mrp.model;

public class Series extends Media {
    public Series(
        int id,
        String title,
        String description,
        int releaseYear,
        int ageRestriction) {
    super(id, title, description, releaseYear, ageRestriction);
    }
}
