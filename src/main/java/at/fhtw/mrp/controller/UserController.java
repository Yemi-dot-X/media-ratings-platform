package at.fhtw.mrp.controller;
import at.fhtw.mrp.exception.ForbiddenException;
import at.fhtw.mrp.exception.UserNotFoundException;
import at.fhtw.mrp.model.User;
import at.fhtw.mrp.service.JwtService;
import at.fhtw.mrp.service.UserService;
import at.fhtw.mrp.util.HttpRequestUtil;
import at.fhtw.mrp.util.HttpResponseUtil;
import com.sun.net.httpserver.HttpExchange;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    //POST /api/users/register
    public void register(HttpExchange exchange) throws IOException {
        User user = HttpRequestUtil.readBody(exchange, User.class);

        User created = userService.register(user);

        exchange.getResponseHeaders().set("Location", "/api/users/" + created.getId() + "/profile");
        HttpResponseUtil.sendJson(exchange, 201, created);
    }

    // POST /api/users/login
    public void login(HttpExchange exchange) throws IOException {
        User credentials = HttpRequestUtil.readBody(exchange, User.class);

        User user = userService.login(credentials.getUsername(), credentials.getPassword());

        String token = jwtService.generateToken(user);
        HttpResponseUtil.sendJson(exchange, 200, Map.of("token", token));
    }

    // GET /api/users/{id}/profile
    public void getProfile(HttpExchange exchange, int id) throws IOException {
        checkOwner(exchange, id);

        User user = userService.findById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        HttpResponseUtil.sendJson(exchange, 200, user);
    }

    // PUT /api/users/{id}/profile
    public void updateProfile(HttpExchange exchange, int id) throws IOException {
        checkOwner(exchange, id);
        User changes = HttpRequestUtil.readBody(exchange, User.class);

        User updated = userService.updateProfile(id, changes);

        HttpResponseUtil.sendJson(exchange, 200, updated);
    }

    private void checkOwner(HttpExchange exchange, int id) {
        int tokenUserId = jwtService.requireUserId(
                exchange.getRequestHeaders().getFirst("Authorization"));
        if (tokenUserId != id) {
            throw new ForbiddenException("Access denied");
        }
    }
}


