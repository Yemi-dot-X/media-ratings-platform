package at.fhtw.mrp.file;
import at.fhtw.mrp.model.Rating;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// zuständig für das Schreiben
public class RatingFileHandler {

    //List uebergibt die Liste wohin was kommt
    public void saveRatingsToFile(String filename, List<Rating> ratings) {

        try (FileWriter writer = new FileWriter(filename)) {

            for (Rating rating : ratings) {

                String line =
                    rating.getId() + ";" +
                    rating.getUserId() + ";" +
                    rating.getMediaId() + ";" +
                    rating.getScore() + ";" +
                    rating.getComment();

                writer.write(line + "\n");
            }

        } catch (IOException e) {
            System.out.println("Fehler beim Speichern: " + e.getMessage());
        }
    }

    public List<Rating> loadRatingsFromFile(String filename) {

        List<Rating> ratings = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filename))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(";");
                if (parts.length != 5) {
                    continue;
                }

                try {
                    int ratingId = Integer.parseInt(parts[0]);
                    int userId = Integer.parseInt(parts[1]);
                    int mediaId = Integer.parseInt(parts[2]);
                    int score = Integer.parseInt(parts[3]);
                    String comment = parts[4];


                    Rating rating = new Rating(
                            ratingId,
                            userId,
                            mediaId,
                            score,
                            comment
                    );

                    ratings.add(rating);
                } catch (NumberFormatException e) {
                    System.err.println(
                            "Ungueltige Bewertung: " + line
                    );
                }

            }
        } catch (IOException e) {
            System.out.println("Fehler beim Laden: " + e.getMessage());
        }

        return ratings;
    }

    public boolean fileExists(String filename) {
        return Files.exists(Path.of(filename));
    }
}
