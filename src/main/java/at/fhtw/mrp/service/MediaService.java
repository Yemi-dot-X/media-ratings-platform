package at.fhtw.mrp.service;

import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Media;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class MediaService {

    private final RatingService ratingService;

    private final List<Media> mediaList = new ArrayList<>();


    // CREATE
    public void createMedia(Media media) {

        if (media == null) {
            throw new IllegalArgumentException("Media must not be null");
        }

        mediaList.add(media);
    }


    // READ ALL
    public List<Media> viewMedia() {
        return mediaList;
    }


    // FIND BY ID
    public Media findById(int id) {

        for (Media media : mediaList) {

            if (media.getId() == id) {
                return media;
            }
        }

        return null;
    }


    // FIND BY TITLE
    public List<Media> findByTitle(String title) {

        List<Media> result = new ArrayList<>();

        for (Media media : mediaList) {

            if (media.getTitle().equalsIgnoreCase(title)) {
                result.add(media);
            }
        }

        return result;
    }


    // FIND BY GENRE
    public List<Media> findByGenre(Genre genre) {

        List<Media> result = new ArrayList<>();

        for (Media media : mediaList) {

            if (media.getGenres().contains(genre)) {
                result.add(media);
            }
        }

        return result;
    }


    // FILTER BY RELEASE YEAR
    public List<Media> filterByReleaseYear(int year) {

        List<Media> result = new ArrayList<>();

        for (Media media : mediaList) {

            if (media.getReleaseYear() == year) {
                result.add(media);
            }
        }

        return result;
    }


    // FILTER BY AGE RESTRICTION
    public List<Media> filterByAgeRestriction(int age) {

        List<Media> result = new ArrayList<>();

        for (Media media : mediaList) {

            if (media.getAgeRestriction() <= age) {
                result.add(media);
            }
        }

        return result;
    }


    // SORT BY TITLE
    public List<Media> sortByTitle() {

        List<Media> result = new ArrayList<>(mediaList);

        result.sort(
                Comparator.comparing(Media::getTitle)
        );

        return result;
    }


    // SORT BY RELEASE YEAR
    public List<Media> sortByReleaseYear() {

        List<Media> result = new ArrayList<>(mediaList);

        result.sort(
                Comparator.comparingInt(Media::getReleaseYear)
        );

        return result;
    }


    // DELETE
    public void deleteMedia(int mediaId, int userId) {

        Media media = findById(mediaId);

        if (media == null) {
            throw new IllegalArgumentException("Media not found");
        }

        if (media.getCreatorId() != userId) {
            throw new IllegalArgumentException(
                    "Only the creator may delete this media"
            );
        }

        mediaList.remove(media);
    }


    // UPDATE
    public void updateMedia(
            int mediaId,
            int userId,
            String newTitle
    ) {

        Media media = findById(mediaId);

        if (media == null) {
            throw new IllegalArgumentException("Media not found");
        }

        if (media.getCreatorId() != userId) {
            throw new IllegalArgumentException(
                    "User is not the creator of this media"
            );
        }

        media.setTitle(newTitle);
    }
}