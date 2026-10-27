package at.fhtw.mrp.service;

import at.fhtw.mrp.model.Rating;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class RatingService {

    //die Liste wird innerhalb der Klasse initialisiert und deshalb
    //benötige ich kein Parameter im der MAIN
    private final List<Rating> ratings = new ArrayList<>();


    // CREATE
    public void createRating(Rating rating) {

        if (rating == null) {
            throw new IllegalArgumentException(
                    "Rating must not be null"
            );
        }

        if (rating.getScore() < 1 || rating.getScore() > 5) {
            throw new IllegalArgumentException(
                    "Score must be between 1 and 5"
            );
        }

        if (rating.getComment() == null ||
                rating.getComment().isBlank()) {
            throw new IllegalArgumentException(
                    "Comment must not be empty"
            );
        }

        ratings.add(rating);
    }


    // FIND BY ID
    public Rating findById(int id) {

        return ratings.stream()
                .filter(rating -> rating.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // ALL RATINGS FOR ONE MEDIA


    public List<Rating> getRatingsByMedia(int mediaId) {
        return ratings.stream()
                .filter(rating -> rating.getMediaId() == mediaId)
                .toList();
    }

    // ALL RATINGS FROM ONE USER

    public List<Rating> getRatingsByUser(int userId) {
        return ratings.stream()
                .filter(rating -> rating.getUserId() == userId)
                .toList();
    }

    // UPDATE
    public void updateRating(
            int ratingId,
            int userId,
            int newScore,
            String newComment
    ) {

        Rating rating = findById(ratingId);

        if (rating == null) {
            throw new IllegalArgumentException("Rating not found");
        }

        if (rating.getUserId() != userId) {
            throw new IllegalArgumentException(
                    "User is not the creator of this rating"
            );
        }

        if (newScore < 1 || newScore > 5) {
            throw new IllegalArgumentException(
                    "Score must be between 1 and 5"
            );
        }

        if (newComment == null || newComment.isBlank()) {
            throw new IllegalArgumentException(
                    "Comment must not be empty"
            );
        }

        rating.setScore(newScore);
        rating.setComment(newComment);
    }


    // DELETE
    public void deleteRating(int ratingId, int userId) {

        Rating rating = findById(ratingId);

        if (rating == null) {
            throw new IllegalArgumentException("Rating not found");
        }

        if (rating.getUserId() != userId) {
            throw new IllegalArgumentException(
                    "User is not the creator of this rating"
            );
        }

        ratings.remove(rating);
    }


    // AVERAGE RATING FOR MEDIA
    public double calculateAverageRating(int mediaId) {

        return ratings.stream()
                .filter(rating -> rating.getMediaId() == mediaId)
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);
    }

    // NUMBER OF RATINGS CREATED BY USER


    public int countRatingsByUser(int userId) {

        return (int) ratings.stream()
                .filter(rating -> rating.getUserId() == userId)
                .count();
    }
}


