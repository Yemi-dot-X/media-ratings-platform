package at.fhtw.mrp.model;

public class Movie extends Media{
    public Movie(int id,
                 String title,
                 String description,
                 int releaseYear,
                 int ageRestriction){
        super(id, title, description, releaseYear, ageRestriction);
    }
}
