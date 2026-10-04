package at.fhtw.mrp.file;
import at.fhtw.mrp.model.Rating;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
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
                                rating.getScore();

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

                int ratingId = Integer.parseInt(parts[0]);
                int userId = Integer.parseInt(parts[1]);
                int mediaId = Integer.parseInt(parts[2]);
                int score = Integer.parseInt(parts[3]);
                String comment = parts[4];
                int stars = Integer.parseInt(parts[5]);

                Rating rating = new Rating(
                        ratingId,
                        userId,
                        mediaId,
                        score,
                        comment,
                        stars
                );

                ratings.add(rating);
            }

        } catch (IOException e) {
            System.out.println("Fehler beim Laden: " + e.getMessage());
        }

        return ratings;
    }
}
