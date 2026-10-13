
package at.fhtw.mrp.file;

import at.fhtw.mrp.model.Movie;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MediaJsonHandlerTest {

    @Test
    void testLoadMovies() {

        // Arrange
        MediaJsonHandler handler = new MediaJsonHandler();

        // Act
        List<Movie> movies =
                handler.loadMovies("src/main/resources/movies.json");
        // Assert
        assertNotNull(movies);
        assertFalse(movies.isEmpty());

        System.out.println("Geladene Filme: " + movies.size());

        for (Movie movie : movies) {
            System.out.println(
                    movie.getId() + " | " +
                            movie.getTitle() + " | " +
                            movie.getGenres()
            );
        }
    }
}
