
package at.fhtw.mrp.service;

import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Media;
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

    // UPDATE
    public void updateMedia(
            int mediaId,
            int userId,
            String newTitle
    ) {

        Media media = findById(mediaId);

        if (media == null) {
            throw new IllegalArgumentException(
                    "Media not found"
            );
        }

        if (media.getCreatorId() != userId) {
            throw new IllegalArgumentException(
                    "User is not the creator of this media"
            );
        }

        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException(
                    "Title must not be empty"
            );
        }

        media.setTitle(newTitle);
    }
}
