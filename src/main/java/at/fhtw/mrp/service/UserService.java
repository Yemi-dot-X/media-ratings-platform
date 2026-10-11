
package at.fhtw.mrp.service;

import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Rating;
import at.fhtw.mrp.model.User;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserService {

    private final RatingService ratingService;
    private final MediaService mediaService;

    private final Map<Integer, User> userMap = new HashMap<>();

    // REGISTER
    public void register(User user) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User must not be null"
            );
        }

        if (user.getUsername() == null ||
                user.getUsername().isBlank()) {
            throw new IllegalArgumentException(
                    "Username must not be empty"
            );
        }

        if (userMap.containsKey(user.getId())) {
            throw new IllegalArgumentException(
                    "User ID already exists"
            );
        }

        if (usernameExists(user.getUsername())) {
            throw new IllegalArgumentException(
                    "Username already exists"
            );
        }

        userMap.put(user.getId(), user);
    }

    // FIND BY ID
    public User findById(int id) {
        return userMap.get(id);
    }

    // FIND BY USERNAME
    public User findByUsername(String username) {

        if (username == null) {
            return null;
        }

        return userMap.values().stream()
                .filter(user ->
                        user.getUsername()
                                .equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
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

        return ratingService.getRatingsByUser(userId)
                .stream()
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);
    }

    // FAVORITE GENRE
    public Genre getFavoriteGenre(int userId) {

        return ratingService.getRatingsByUser(userId)
                .stream()
                .map(rating ->
                        mediaService.findById(rating.getMediaId()))
                .filter(Objects::nonNull)
                .filter(media -> media.getGenres() != null)
                .flatMap(media -> media.getGenres().stream())
                .collect(Collectors.groupingBy(
                        genre -> genre,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // LEADERBOARD
    public List<User> getLeaderboard() {

        return userMap.values().stream()
                .sorted(
                        Comparator.comparingInt(
                                (User user) ->
                                        getTotalRatings(user.getId())
                        ).reversed()
                )
                .toList();
    }
}
