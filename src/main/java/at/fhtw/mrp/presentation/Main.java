
package at.fhtw.mrp.presentation;

import at.fhtw.mrp.file.MediaJsonHandler;
import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Movie;
import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.service.MediaService;
import at.fhtw.mrp.service.RatingService;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. Services und Handler erstellen
        RatingService ratingService = new RatingService();

        MediaService mediaService =
                new MediaService(ratingService);

        MediaJsonHandler jsonHandler = new MediaJsonHandler();

        // 2. Filme aus JSON laden
        List<Movie> movies =
                jsonHandler.loadMovies("movies.json");

        // 3. Filme in MediaService speichern
        for (Movie movie : movies) {
            mediaService.createMedia(movie);
        }

        // 4. Alle Filme ausgeben
        System.out.println("=== ALLE FILME ===");

        for (Media media : mediaService.viewMedia()) {
            System.out.println(
                    media.getId() + " | " +
                            media.getTitle() + " | " +
                            media.getReleaseYear() + " | " +
                            media.getGenres()
            );
        }

        // 5. Film nach ID suchen
        System.out.println("\n=== SUCHE NACH ID ===");

        Media found = mediaService.findById(1);

        if (found != null) {
            System.out.println(found.getTitle());
        } else {
            System.out.println("Film nicht gefunden");
        }

        // 6. Filme nach Genre filtern
        System.out.println("\n=== GENRE ACTION ===");

        List<Media> actionMovies =
                mediaService.findByGenre(Genre.ACTION);

        for (Media media : actionMovies) {
            System.out.println(media.getTitle());
        }

        // 7. Filme nach Erscheinungsjahr sortieren
        System.out.println("\n=== SORTIERT NACH JAHR ===");

        for (Media media : mediaService.sortByReleaseYear()) {
            System.out.println(
                    media.getReleaseYear() + " | " +
                            media.getTitle()
            );
        }

        // 8. Anzahl der Action-Filme
        System.out.println("\n=== ACTION FILME ZÄHLEN ===");

        long count =
                mediaService.countMediaByGenre(Genre.ACTION);

        System.out.println("Anzahl: " + count);
    }
}
