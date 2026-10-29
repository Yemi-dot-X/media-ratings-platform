package at.fhtw.mrp.server;

import at.fhtw.mrp.dto.LoginRequest;
import at.fhtw.mrp.dto.RegisterRequest;
import at.fhtw.mrp.dto.UserProfileResponse;
import at.fhtw.mrp.exception.InvalidCredentialsException;
import at.fhtw.mrp.exception.UserAlreadyExistsException;
import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.model.User;
import at.fhtw.mrp.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import static at.fhtw.mrp.util.HttpResponseUtil.sendText;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class UserHandler implements HttpHandler {

    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

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

            // Erfolgreicher Login
            sendText(exchange, 200, "Login successful");

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

}