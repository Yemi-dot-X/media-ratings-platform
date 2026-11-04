
package at.fhtw.mrp.service;

import at.fhtw.mrp.dto.UpdateProfileRequest;
import at.fhtw.mrp.exception.InvalidCredentialsException;
import at.fhtw.mrp.exception.UserAlreadyExistsException;
import at.fhtw.mrp.exception.UserNotFoundException;
import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Rating;
import at.fhtw.mrp.model.User;
import at.fhtw.mrp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;


import java.time.LocalDate;
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

    private final UserRepository userRepository;


    // REGISTER
    public void register(User user) {
        // User prüfen
        if (user == null) {
            throw new IllegalArgumentException(
                    "User must not be null"
            );
        }

        // Username prüfen
        if (user.getUsername() == null ||
                user.getUsername().isBlank()) {
            throw new IllegalArgumentException(
                    "Username must not be empty"
            );
        }

        // E-Mail prüfen
        if (user.getEmail() == null ||
                user.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "Email must not be empty"
            );
        }

// Passwort prüfen
        if (user.getPassword() == null ||
                user.getPassword().isBlank()) {
            throw new IllegalArgumentException(
                    "Password must not be empty"
            );
        }
        if (user.getDateOfBirth() == null) {
            throw new IllegalArgumentException(
                    "Date of birth is required"
            );
        }

        if (user.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Date of birth cannot be in the future"
            );
        }

        // Username bereits vorhanden?
        if (usernameExists(user.getUsername())) {
            throw new UserAlreadyExistsException(
                    "Username already exists"
            );
        }

        // E-Mail bereits vorhanden?
        if (emailExists(user.getEmail())) {
            throw new UserAlreadyExistsException(
                    "Email already exists"
            );
        }



        // Passwort hashen
        //eine zufalliger Salt wird verwendet
        //sodass das unterschiedliche Hashes entstehen bei den selben Passwort

        String hashedPassword = BCrypt.hashpw(
                user.getPassword(),
                BCrypt.gensalt()
        );

        user.setPassword(hashedPassword);

// ID vergeben
        user.setId(0);
        userRepository.save(user);

// Benutzer speichern
        userRepository.save(user);    }

    public User login(String username, String password){
        if(username == null || username.isBlank()){
            throw new IllegalArgumentException("Username is required");
        }

        if(password == null || password.isBlank()){
            throw new IllegalArgumentException("Password is required");
        }

        User user = findByUsername(username);

        if(user ==  null){
            throw new InvalidCredentialsException("Invalid credentials");
        }

        // Passwort prüfen
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
            return user;
    }

    // FIND BY ID
    public User findById(int userId) {
        return userRepository.findById(userId);
    }

    // FIND BY USERNAME
    public User findByUsername(String username) {

        if (username == null) {
            return null;
        }
       return userRepository.findByUsername(username);
    }

    public boolean emailExists(String email) {

        return userRepository.existsByEmail(email);

    }

    // CHECK USERNAME
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public User updateProfile(int userId, UpdateProfileRequest request){
        //User suchen
        User user = userRepository.findById(userId);

        if(user == null){
            throw new UserNotFoundException("User not found");
        }

        if(request == null){
            throw new IllegalArgumentException("Request is null");
        }

        if(request.getName() == null || request.getName().isBlank()){
            throw new IllegalArgumentException(" Name is empty");
        }


        if(request.getBirthDate() == null || request.getBirthDate().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Invalid birth date");

        }

        // E-Mail validieren
        if (request.getEmail() == null ||
                request.getEmail().isBlank() ||
                !request.getEmail().contains("@")) {

            throw new IllegalArgumentException("Invalid email");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setDateOfBirth(request.getBirthDate());
        userRepository.save(user);

        return user;
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

        return userRepository.findAll().
                stream()
                .sorted(
                        Comparator.comparingInt(
                                (User user) ->
                                        getTotalRatings(user.getId())
                        ).reversed()
                )
                .toList();
    }
}
