package at.fhtw.mrp.model;

public class Rating {
    private int id;
    private User user;
    private Media media;
    private int stars;
    private String comments;

    public Rating(int id, User user, Media media,
                  int stars, String comments) {
        this.id = id;
        this.user = user;
        this.media = media;
        this.stars =stars;
        this.comments = comments;
    }
}
