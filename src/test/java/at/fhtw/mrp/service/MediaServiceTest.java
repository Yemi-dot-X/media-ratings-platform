
package at.fhtw.mrp.service;

import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Movie;
import at.fhtw.mrp.model.Media;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MediaServiceTest {

    @Test
    void findById_shouldReturnMovie() {

        // Arrange: Testdaten vorbereiten
        RatingService ratingService = new RatingService();
        MediaService mediaService =
                new MediaService(ratingService);

        Movie movie = new Movie(
                1,
                101,
                "Inception",
                "Science Fiction",
                2010,
                12,
                List.of(Genre.ACTION),
                148
        );

        mediaService.createMedia(movie);

        // Act: Methode ausführen
        Media result = mediaService.findById(101);

        // Assert: Ergebnis überprüfen
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
    }

    @Test
    void findById_shouldReturnNull() {

        // Arrange
        RatingService ratingService = new RatingService();
        MediaService mediaService =
                new MediaService(ratingService);

        // Act
        Media result = mediaService.findById(999);

        // Assert
        assertNull(result);
    }
}
