package at.fhtw.mrp.file;

import at.fhtw.mrp.model.Movie;
import at.fhtw.mrp.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaJsonHandler {
    private final ObjectMapper mapper = new ObjectMapper();

    public void saveMovies(List<Movie> movies, String filename) {

        try {

            mapper.writeValue(
                    new File(filename),
                    movies
            );

        } catch (IOException e) {

            System.out.println(
                    "Fehler beim Serialisieren: "
                            + e.getMessage()
            );
        }
    }

    public List<Movie> loadMovies(String filename) {

        try {

            return mapper.readValue(
                    new File(filename),
                    new TypeReference<List<Movie>>() {}
            );

        } catch (IOException e) {

            System.out.println(
                    "Fehler beim Deserialisieren: "
                            + e.getMessage()
            );

            return new ArrayList<>();
        }
    }

    public List<Game> loadGames(String filename) {
        try {
            return mapper.readValue(
                    new File(filename),
                    new TypeReference<List<Game>>() {}
            );
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Fehler beim Laden der Spiele", e
            );
        }
    }
}