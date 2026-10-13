package at.fhtw.mrp.file;

import at.fhtw.mrp.model.Movie;

import java.io.*;
import java.util.*;

public class MediaCsvHandler {


    public List<Movie> loadMoviesFromCsv(String path)
            throws IOException {

        List<Movie> movies = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(path))) {

            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);
                String title = data[1];
                int year = Integer.parseInt(data[2]);
                int age = Integer.parseInt(data[3]);

                // Movie-Objekt erstellen
                Movie movie = new Movie(
                        0,                  // creatorId
                        id,
                        title,
                        "",                 // description
                        year,
                        age,
                        new ArrayList<>(),  // genres
                        0                   // duration
                );

                // Film zur Liste hinzufügen
                movies.add(movie);
            }
        }

        return movies;
    }
}
