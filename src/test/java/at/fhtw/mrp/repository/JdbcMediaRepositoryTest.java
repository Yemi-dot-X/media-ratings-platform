package at.fhtw.mrp.repository;

import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.model.MediaType;
import at.fhtw.mrp.repository.JdbcMediaRepository;
import at.fhtw.mrp.repository.MediaRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcMediaRepositoryTest {

    @Test
    void readById_shouldReturnNull_whenMediaDoesNotExist() {

        // Arrange
        MediaRepository repository = new JdbcMediaRepository();

        // Act
        Media result = repository.readById(-1);

        // Assert
        assertNull(result);
    }


    @Test
    void create_shouldSaveMedia() {

        // Arrange
        MediaRepository repository = new JdbcMediaRepository();

        Media media = new Media(
                0,
                1,  // creatorId = testuser
                "Inception",
                "Science Fiction",
                2010,
                12,
                List.of(Genre.ACTION),
                MediaType.MOVIE,
                148
        );

        // Act
        Media savedMedia = repository.create(media);

        // Assert
        assertNotNull(savedMedia);
        assertTrue(savedMedia.getId() > 0);
        assertEquals("Inception", savedMedia.getTitle());

        // Cleanup
        repository.deleteById(savedMedia.getId());
    }

    @Test
    void readById_shouldReturnSavedMedia() {

        // Arrange
        MediaRepository repository = new JdbcMediaRepository();

        Media media = new Media(
                0,
                1,
                "Inception",
                "Science Fiction",
                2010,
                12,
                List.of(Genre.ACTION),
                MediaType.MOVIE,
                148
        );

        Media savedMedia = repository.create(media);

        try {
            // Act
            Media result = repository.readById(savedMedia.getId());

            // Assert
            assertNotNull(result);
            assertEquals(savedMedia.getId(), result.getId());
            assertEquals("Inception", result.getTitle());

        } finally {
            // Testdaten entfernen
            repository.deleteById(savedMedia.getId());
        }
    }


    @Test
    void updateById_shouldChangeTitle() {

        // Arrange
        MediaRepository repository = new JdbcMediaRepository();

        Media media = new Media(
                0,
                1,
                "Inception",
                "Science Fiction",
                2010,
                12,
                List.of(Genre.ACTION),
                MediaType.MOVIE,
                148
        );

        Media savedMedia = repository.create(media);

        try {
            // Titel ändern
            savedMedia.setTitle("Inception Updated");

            // Act
            Media updatedMedia = repository.updateById(
                    savedMedia.getId(),
                    savedMedia
            );

            // Assert
            assertNotNull(updatedMedia);
            assertEquals("Inception Updated", updatedMedia.getTitle());

            // Kontrollieren, ob die Änderung in PostgreSQL steht
            Media result = repository.readById(savedMedia.getId());

            assertNotNull(result);
            assertEquals("Inception Updated", result.getTitle());

        } finally {
            repository.deleteById(savedMedia.getId());
        }
    }


    @Test
    void deleteById_shouldRemoveMedia() {

        // Arrange
        MediaRepository repository = new JdbcMediaRepository();

        Media media = new Media(
                0,
                1,
                "Inception",
                "Science Fiction",
                2010,
                12,
                List.of(Genre.ACTION),
                MediaType.MOVIE,
                148
        );

        Media savedMedia = repository.create(media);

        // Act
        boolean deleted = repository.deleteById(savedMedia.getId());

        // Assert
        assertTrue(deleted);

        Media result = repository.readById(savedMedia.getId());

        assertNull(result);
    }


    @Test
    void search_shouldFindMediaByTitle() {

        // Arrange
        MediaRepository repository = new JdbcMediaRepository();

        Media media = new Media(
                0,
                1,
                "Inception",
                "Science Fiction",
                2010,
                12,
                List.of(Genre.ACTION),
                MediaType.MOVIE,
                148
        );

        Media savedMedia = repository.create(media);

        try {
            MediaFilter filter = new MediaFilter();
            filter.setTitle("Inception");

            // Act
            List<Media> results = repository.search(filter);

            // Assert
            assertTrue(
                    results.stream().anyMatch(
                            result -> result.getId() == savedMedia.getId()
                    )
            );

        } finally {
            repository.deleteById(savedMedia.getId());
        }
    }

}