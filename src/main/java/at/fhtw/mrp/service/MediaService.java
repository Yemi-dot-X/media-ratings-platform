package at.fhtw.mrp.service;

import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Media;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MediaService {

    private final RatingService ratingService;

    private final List<Media> mediaList = new ArrayList<>();


    public void createMedia(Media media) {
        mediaList.add(media);
    }


    public List<Media> viewMedia() {
        return mediaList;
    }


    public Media findMediaById(int id) {

        for (Media media : mediaList) {

            if (media.getId() == id) {
                return media;
            }
        }

        return null;
    }


    public void deleteMedia(int mediaId, int userId) {

        Media media = findMediaById(mediaId);

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


    public List<Media> findByGenre(Genre genre) {

        List<Media> result = new ArrayList<>();

        for (Media media : mediaList) {

            if (media.getGenres().contains(genre)) {
                result.add(media);
            }
        }

        return result;
    }


    public void updateMedia(int mediaId, int userId, String newTitle) {

        Media media = findMediaById(mediaId);

        if(media == null){
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