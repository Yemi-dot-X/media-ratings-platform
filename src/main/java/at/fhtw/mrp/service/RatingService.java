package at.fhtw.mrp.service;

public class RatingService {

    private List<Rating> ratings = new ArrayList<>();

    public List<Rating> getRatingsByUser(int userId) {
        return ratings.stream()
                .filter(rating -> rating.getUserId() == userId)
                .toList();
    }
}
