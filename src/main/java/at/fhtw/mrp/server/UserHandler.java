package at.fhtw.mrp.server;

import at.fhtw.mrp.dto.RegisterRequest;
import at.fhtw.mrp.exception.UserAlreadyExistsException;
import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.model.User;
import at.fhtw.mrp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import static at.fhtw.mrp.util.HttpResponseUtil.sendText;

import java.io.IOException;
import java.io.InputStream;

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

        try {
            ObjectMapper mapper = new ObjectMapper();
            //readValue(Quelle, Zielklasse)
            RegisterRequest request = mapper.readValue(
                    exchange.getRequestBody(),
                    RegisterRequest.class
            );

            // User-Objekt erstellen
            User user = new User(
                    0,
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword()
            );

            //Server aufrufen
            userService.register(user);

            sendText(exchange, 201, "User created");

        } catch (UserAlreadyExistsException e) {

            sendText(exchange, 409, e.getMessage());

        } catch (IllegalArgumentException e) {

            sendText(exchange, 400, e.getMessage());
        }
    }
    private void handleGet(HttpExchange exchange)
            throws IOException {
        // Benutzerprofil abrufen
    }
}