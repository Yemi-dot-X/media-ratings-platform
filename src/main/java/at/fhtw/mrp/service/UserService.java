package at.fhtw.mrp.service;

import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Rating;
import at.fhtw.mrp.model.User;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserService {

    private final RatingService ratingService;

    private final List<User> users = new ArrayList<>();


    // REGISTER
    public void register(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        if (usernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        users.add(user);
    }


    // FIND BY ID
    public User findById(int id) {

        for (User user : users) {

            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }


    // FIND BY USERNAME
    public User findByUsername(String username) {

        for (User user : users) {

            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }

        return null;
    }


    // CHECK USERNAME
    public boolean usernameExists(String username) {

        return findByUsername(username) != null;
    }


    // TOTAL RATINGS OF USER
    public int getTotalRatings(int userId) {

        return ratingService.countRatingsByUser(userId);
    }


    // AVERAGE RATING GIVEN BY USER
    public double getAverageRating(int userId) {

        List<Rating> ratings =
                ratingService.getRatingsByUser(userId);

        if (ratings.isEmpty()) {
            return 0.0;
        }

        double sum = 0;

        for (Rating rating : ratings) {
            sum += rating.getScore();
        }

        return sum / ratings.size();
    }


    // FAVORITE GENRE
    public Genre getFavoriteGenre(int userId) {

        // später ergänzen
        return null;
    }


    // LEADERBOARD
    public List<User> getLeaderboard() {

        List<User> result = new ArrayList<>(users);

        result.sort(
                (user1, user2) ->
                        Integer.compare(
                                getTotalRatings(user2.getId()),
                                getTotalRatings(user1.getId())
                        )
        );

        return result;
    }
}