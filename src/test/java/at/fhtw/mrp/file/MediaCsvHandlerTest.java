package at.fhtw.mrp.file;

import at.fhtw.mrp.model.Movie;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MediaCsvHandlerTest {

    @Test
    void testLoadMoviesFromCsv() throws IOException {

        MediaCsvHandler csvHandler = new MediaCsvHandler();

        List<Movie> movies = csvHandler.loadMoviesFromCsv(
                "src/main/resources/movies.csv"
        );

        assertNotNull(movies);
        assertFalse(movies.isEmpty());

        for (Movie movie : movies) {
            System.out.println(movie.getTitle());
        }
    }
}
