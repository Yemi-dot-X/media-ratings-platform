
package at.fhtw.mrp.service;

import at.fhtw.mrp.exception.ForbiddenException;
import at.fhtw.mrp.exception.NotFoundException;
import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.model.MediaType;
import at.fhtw.mrp.repository.MediaFilter;
import at.fhtw.mrp.repository.MediaRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;


    // CREATE
    public Media create(Media media, int creatorId) {

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

        if (media.getAgeRestriction() != null &&
                media.getAgeRestriction() < 0) {
            throw new IllegalArgumentException(
                    "Age restriction must not be negative"
            );
        }

        if (media.getDuration() != null) {
            checkDuration(media.getMediaType(), media.getDuration());
        }

        if (media.getGenres() == null) {
            media.setGenres(new ArrayList<>());
        }

        media.setCreatorId(creatorId);
        return mediaRepository.create(media);
    }


    // READ ALL (mit Suche, Filter und Sortierung)
    public List<Media> search(MediaFilter filter) {
        return mediaRepository.search(filter == null ? new MediaFilter() : filter);
    }

    // FIND BY ID
    public Media findById(int id) {

        return mediaRepository.readById(id);
    }

    // UPDATE für PUT
    public Media update(int mediaId, int userId, Media changes) {

        Media media = findById(mediaId);

        if (media == null) {
            throw new NotFoundException("Media not found");
        }

        if (media.getCreatorId() != userId) {
            throw new ForbiddenException("Only the creator may update this media");
        }

        if (changes == null) {
            throw new IllegalArgumentException("Request is null");
        }

        if (changes.getTitle() != null) {
            if (changes.getTitle().isBlank()) {
                throw new IllegalArgumentException("Title must not be empty");
            }
            media.setTitle(changes.getTitle());
        }

        if (changes.getDescription() != null) {
            media.setDescription(changes.getDescription());
        }

        if (changes.getGenres() != null) {
            media.setGenres(changes.getGenres());
        }

        if (changes.getReleaseYear() != null) {
            media.setReleaseYear(changes.getReleaseYear());
        }

        if (changes.getAgeRestriction() != null) {
            if (changes.getAgeRestriction() < 0) {
                throw new IllegalArgumentException("Age restriction must not be negative");
            }
            media.setAgeRestriction(changes.getAgeRestriction());
        }

        if (changes.getDuration() != null) {
            checkDuration(media.getMediaType(), changes.getDuration());
            media.setDuration(changes.getDuration());
        }

        return mediaRepository.updateById(mediaId, media);
    }

    // DELETE
    public void delete(int mediaId, int userId) {

        Media media = findById(mediaId);

        if (media == null) {
            throw new NotFoundException("Media not found");
        }

        if (media.getCreatorId() != userId) {
            throw new ForbiddenException("Only the creator may delete this media");
        }

        mediaRepository.deleteById(mediaId);
    }

    private void checkDuration(MediaType type, int duration) {
        if (type != MediaType.MOVIE) {
            throw new IllegalArgumentException("Duration is only supported for movies");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
    }
}
