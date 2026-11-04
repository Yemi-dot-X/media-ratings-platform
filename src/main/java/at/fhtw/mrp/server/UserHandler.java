package at.fhtw.mrp.server;

import at.fhtw.mrp.dto.*;
import at.fhtw.mrp.exception.InvalidCredentialsException;
import at.fhtw.mrp.exception.UserAlreadyExistsException;
import at.fhtw.mrp.exception.UserNotFoundException;
import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.model.User;
import at.fhtw.mrp.repository.InMemoryUserRepository;
import at.fhtw.mrp.repository.UserRepository;
import at.fhtw.mrp.service.JwtService;
import at.fhtw.mrp.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import static at.fhtw.mrp.util.HttpResponseUtil.sendText;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@RequiredArgsConstructor
public class UserHandler implements HttpHandler {

    private final UserService userService;
    private final JwtService jwtService;



    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        String method = exchange.getRequestMethod();

        switch (method) {
            case "POST":
                handlePost(exchange);
                break;

            case "GET":
                handleGet(exchange);
                break;

            case "PUT":
                handlePut(exchange);
                break;

            default:
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
        }
    }

    private void handlePost(HttpExchange exchange)
            throws IOException {

        String path = exchange.getRequestURI().getPath();


        switch (path) {

            case "/api/users/register":
                handleRegister(exchange);
                break;

            case "/api/users/login":
                handleLogin(exchange);
                break;

            default:
                sendText(exchange, 404, "Not found");
                break;
        }
    }


    private void handleRegister(HttpExchange exchange)
            throws IOException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            //readValue(Quelle, Zielklasse)
            RegisterRequest request = mapper.readValue(
                    exchange.getRequestBody(),
                    RegisterRequest.class
            );

            // User-Objekt erstellen
            User user = new User();

            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setDateOfBirth(request.getDateOfBirth());
            //Server aufrufen
            userService.register(user);

            sendText(exchange, 201, "User created");

        } catch (UserAlreadyExistsException e) {

            sendText(exchange, 409, e.getMessage());

        } catch (IllegalArgumentException e) {

            sendText(exchange, 400, e.getMessage());
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            sendText(exchange, 400, "Invalid JSON");

        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, 500, "Internal server error");
        }
    }
    private void handleGet(HttpExchange exchange)
            throws IOException {

        Integer tokenUserId = authenticate(exchange);

        if(tokenUserId == null){
            return;
        }

        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        if (parts.length != 5 ||
                !parts[1].equals("api") ||
                !parts[2].equals("users") ||
                !parts[4].equals("profile")) {

            sendText(exchange, 404, "Not found");
            return;
        }

            int userId;
        try {
            userId = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            sendText(exchange, 400, "Invalid user ID");
            return;
        }

        if (userId <= 0) {
            sendText(exchange, 400, "Invalid user ID");
            return;
        }
            if(userId != tokenUserId){
                sendText(exchange, 403, "Access denied");
                return;
            }

            User user = userService.findById(userId);
            if(user == null ){
                sendText(exchange, 404, "User not found");
                return;
            }

        UserProfileResponse response = new UserProfileResponse();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setName(user.getName());
            response.setEmail(user.getEmail());
            response.setDateOfBirth(user.getDateOfBirth());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );

// DTO in JSON umwandeln
        String json = mapper.writeValueAsString(response);

// JSON als HTTP-Antwort senden

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json; charset=UTF-8"
        );

        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }

    }

    public void handleLogin(HttpExchange exchange)
            throws IOException {

        try {
            ObjectMapper mapper = new ObjectMapper();

            // JSON in LoginRequest umwandeln
            LoginRequest request = mapper.readValue(
                    exchange.getRequestBody(),
                    LoginRequest.class
            );

            // UserService aufrufen
            User user = userService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            String token = jwtService.generateToken(user);
            LoginResponse response = new LoginResponse(token);
            String json = mapper.writeValueAsString(response);
            byte[] responseBytes =
                    json.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set(
                    "Content-Type",
                    "application/json; charset=UTF-8"
            );

            exchange.sendResponseHeaders(200, responseBytes.length);


            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }




        } catch (InvalidCredentialsException e) {
            sendText(exchange, 401, "Invalid credentials");

        } catch (JsonProcessingException e) {
            sendText(exchange, 400, "Invalid JSON");

        } catch (IllegalArgumentException e) {
            sendText(exchange, 400, e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, 500, "Internal server error");
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            // JWT prüfen und User-ID aus Token holen
            Integer tokenUserId = authenticate(exchange);

            if (tokenUserId == null) {
                return;
            }

            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");

            if (parts.length != 5 ||
                    !parts[1].equals("api") ||
                    !parts[2].equals("users") ||
                    !parts[4].equals("profile")) {
                sendText(exchange, 404, "Invalid profile path");
                return;
            }

            int userId = Integer.parseInt(parts[3]);
            // Berechtigung prüfen
            if (userId != tokenUserId) {
                sendText(exchange, 403, "Access denied");
                return;
            }
            // JSON in Java-Objekt umwandeln
            UpdateProfileRequest request = mapper.readValue(
                    exchange.getRequestBody(),
                    UpdateProfileRequest.class
            );

            // Service aufrufen
            User updatedUser = userService.updateProfile(userId, request);

            //Antwort als JSON erstellen
// DTO erstellen
            UserProfileResponse responseDto = new UserProfileResponse();

// Daten übernehmen
            responseDto.setId(updatedUser.getId());
            responseDto.setUsername(updatedUser.getUsername());
            responseDto.setName(updatedUser.getName());
            responseDto.setEmail(updatedUser.getEmail());
            responseDto.setDateOfBirth(updatedUser.getDateOfBirth());

// DTO in JSON umwandeln
            String response = mapper.writeValueAsString(responseDto);

            byte[] responseBytes =
                    response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set(
                    "Content-Type", "application/json; charset=UTF-8"
            );

            exchange.sendResponseHeaders(200, responseBytes.length);

            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(responseBytes);
            }

        } catch (UserNotFoundException e) {
            sendText(exchange, 404, e.getMessage());

        } catch (IllegalArgumentException e) {
            sendText(exchange, 400, e.getMessage());

        } catch (JsonProcessingException e) {
            sendText(exchange, 400, "Invalid JSON");
        }
    }

    private Integer authenticate(HttpExchange exchange) throws IOException {

        // 1. Authorization-Header lesen
        String authHeader = exchange.getRequestHeaders()
                .getFirst("Authorization");

        // 2. Header prüfen
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendText(exchange, 401, "Missing or invalid token");
            return null;
        }

        // 3. JWT aus Header holen
        String token = authHeader.substring(7);

        // 4. JWT validieren
        if (!jwtService.validateToken(token)) {
            sendText(exchange, 401, "Invalid token");
            return null;
        }

        // 5. User-ID aus JWT zurückgeben
        try {
            return jwtService.getUserIdFromToken(token);
        } catch (JwtException | IllegalArgumentException e) {
            sendText(exchange, 401, "Invalid token");
            return null;
        }
    }
}