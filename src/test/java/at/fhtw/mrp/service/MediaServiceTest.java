package at.fhtw.mrp.service;

import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.model.MediaType;
import at.fhtw.mrp.repository.JdbcMediaRepository;
import at.fhtw.mrp.repository.MediaRepository;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MediaServiceTest {

    @Test
    void findById_shouldReturnMedia() {

        // Arrange
        MediaRepository mediaRepository =
                new JdbcMediaRepository();

        MediaService mediaService =
                new MediaService(mediaRepository);

        Media media = new Media(
                0,
                101,
                "Inception",
                "Science Fiction",
                2010,
                12,
                List.of(Genre.ACTION),
                MediaType.MOVIE,
                148
        );

        // Act
        Media created = mediaService.create(media, 101);

        Media result = mediaService.findById(created.getId());

        // Assert
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        assertEquals(MediaType.MOVIE, result.getMediaType());
    }
}