package at.fhtw.mrp.repository;

import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.model.MediaType;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
// Suchkriterien für GET /api/media. Nicht gesetzte Felder (null) filtern nicht.
// sortBy ist "title" oder "releaseYear". Ohne sortBy sortiert der Server nach id.

@Getter
@Setter
public class MediaFilter {

    private String title;
    private Genre genre;
    private MediaType mediaType;
    private Integer releaseYear;
    //Zeigt Medien mit ageRestriction kleiner oder gleich diesem Wert.
    private Integer ageRestriction;
    private String sortBy;

    public void setSortBy(String sortBy) {
        if (sortBy != null && !sortBy.equals("title") && !sortBy.equals("releaseYear")) {
            throw new IllegalArgumentException("sortBy must be title or releaseYear");
        }
        this.sortBy = sortBy;
    }

    //Wird vom In-Memory Repository benutzt.
    // Das JDBC Repository baut dieselben Regeln als SQL.
    public boolean matches(Media media) {
        if (title != null && (media.getTitle() == null
                || !media.getTitle().toLowerCase().contains(title.toLowerCase()))) {
            return false;
        }
        if (genre != null && (media.getGenres() == null || !media.getGenres().contains(genre))) {
            return false;
        }
        if (mediaType != null && media.getMediaType() != mediaType) {
            return false;
        }
        if (releaseYear != null && !releaseYear.equals(media.getReleaseYear())) {
            return false;
        }
        return ageRestriction == null
                || (media.getAgeRestriction() != null && media.getAgeRestriction() <= ageRestriction);
    }

    public Comparator<Media> comparator() {
        if ("title".equals(sortBy)) {
            return Comparator.comparing(Media::getTitle, String.CASE_INSENSITIVE_ORDER);
        }
        if ("releaseYear".equals(sortBy)) {
            return Comparator.comparing(Media::getReleaseYear,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        }
        return Comparator.comparingInt(Media::getId);
    }
}


