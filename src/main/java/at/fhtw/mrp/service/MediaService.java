
package at.fhtw.mrp.service;

import at.fhtw.mrp.dto.UpdateMediaRequest;
import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.model.MediaType;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class MediaService {

    private final RatingService ratingService;

    private final Map<Integer, Media> mediaMap = new HashMap<>();

    // CREATE
    public void createMedia(Media media) {

        if (media == null) {
            throw new IllegalArgumentException(
                    "Media must not be null"
            );
        }

        if (media.getTitle() == null ||
                media.getTitle().isBlank()) {
            throw new IllegalArgumentException(
                    "Title must not be empty"
            );
        }

        if (mediaMap.containsKey(media.getId())) {
            throw new IllegalArgumentException(
                    "Media ID already exists"
            );
        }

        mediaMap.put(media.getId(), media);
    }

    // READ ALL
    public List<Media> viewMedia() {
        return new ArrayList<>(mediaMap.values());
    }

    // FIND BY ID
    public Media findById(int id) {
        return mediaMap.get(id);
    }

    // FIND BY TITLE
    public List<Media> findByTitle(String title) {
        return mediaMap.values().stream()
                .filter(media ->
                        media.getTitle().equalsIgnoreCase(title))
                .toList();
    }

    // FIND BY GENRE
    public List<Media> findByGenre(Genre genre) {
        return mediaMap.values().stream()
                .filter(media ->
                        media.getGenres() != null &&
                                media.getGenres().contains(genre))
                .toList();
    }

    // FILTER BY RELEASE YEAR
    public List<Media> filterByReleaseYear(int year) {
        return mediaMap.values().stream()
                .filter(media ->
                        media.getReleaseYear() == year)
                .toList();
    }

    // FILTER BY AGE RESTRICTION
    public List<Media> filterByAgeRestriction(int age) {
        return mediaMap.values().stream()
                .filter(media ->
                        media.getAgeRestriction() <= age)
                .toList();
    }

    // SORT BY TITLE
    public List<Media> sortByTitle() {
        return mediaMap.values().stream()
                .sorted(Comparator.comparing(Media::getTitle))
                .toList();
    }

    // SORT BY RELEASE YEAR
    public List<Media> sortByReleaseYear() {
        return mediaMap.values().stream()
                .sorted(Comparator.comparingInt(Media::getReleaseYear))
                .toList();
    }

    // COUNT BY GENRE
    public long countMediaByGenre(Genre genre) {
        return mediaMap.values().stream()
                .filter(media ->
                        media.getGenres() != null &&
                                media.getGenres().contains(genre))
                .count();
    }

    // DELETE
    public void deleteMedia(int mediaId, int userId) {

        Media media = findById(mediaId);

        if (media == null) {
            throw new IllegalArgumentException(
                    "Media not found"
            );
        }

        if (media.getCreatorId() != userId) {
            throw new IllegalArgumentException(
                    "Only the creator may delete this media"
            );
        }

        mediaMap.remove(mediaId);
    }

    // UPDATE für PUT handle
    public Media updateMedia(
            int mediaId,
            int userId,
            Media changes
    ) {
        Media media = findById(mediaId);

        // 1. Existiert das Medium?
        if (media == null) {
            throw new IllegalArgumentException("Media not found");
        }

        // 2. Ist der Benutzer der Ersteller?
        if (media.getCreatorId() != userId) {
            throw new IllegalArgumentException(
                    "Only the creator may update this media"
            );
        }

        if (changes == null) {
            throw new IllegalArgumentException("Request is null");
        }

        // 3. Titel aktualisieren
        if (changes.getTitle() != null) {
            if (changes.getTitle().isBlank()) {
                throw new IllegalArgumentException("Title must not be empty");
            }
            media.setTitle(changes.getTitle());
        }

        // 4. Beschreibung aktualisieren
        if (changes.getDescription() != null) {
            media.setDescription(changes.getDescription());
        }

        // 5. Genres aktualisieren
        if (changes.getGenres() != null) {
            media.setGenres(changes.getGenres());
        }

        // 6. Erscheinungsjahr aktualisieren
        if (changes.getReleaseYear() != null) {
            media.setReleaseYear(changes.getReleaseYear());
        }

        // 7. Altersfreigabe aktualisieren
        if (changes.getAgeRestriction() != null) {
            media.setAgeRestriction(changes.getAgeRestriction());
        }

        // 8. Filmdauer aktualisieren
        if (changes.getDuration() != null) {
            if (media.getType() != MediaType.MOVIE) {
                throw new IllegalArgumentException(
                        "Duration is only supported for movies"
                );
            }

            if (changes.getDuration() <= 0) {
                throw new IllegalArgumentException("Duration must be positive");
            }

            media.setDuration(changes.getDuration());
        }

        return media;
    }
}
