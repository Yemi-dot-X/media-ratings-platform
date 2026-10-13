
package at.fhtw.mrp.file;

import at.fhtw.mrp.model.Movie;
import at.fhtw.mrp.model.Game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MediaJsonHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    // SAVE MOVIES
    public void saveMovies(List<Movie> movies, String filename) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(filename), movies);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Fehler beim Speichern der Filme: " + filename, e
            );
        }
    }

    // LOAD MOVIES
    public List<Movie> loadMovies(String filename) {
        try {
            return mapper.readValue(
                    new File(filename),
                    new TypeReference<List<Movie>>() {}
            );

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Fehler beim Laden der Filme: " + filename, e
            );
        }
    }

    // LOAD GAMES
    public List<Game> loadGames(String filename) {
        try {
            return mapper.readValue(
                    new File(filename),
                    new TypeReference<List<Game>>() {}
            );

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Fehler beim Laden der Spiele: " + filename, e
            );
        }
    }
}
