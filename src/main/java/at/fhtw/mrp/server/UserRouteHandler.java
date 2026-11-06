package at.fhtw.mrp.server;

import at.fhtw.mrp.controller.UserController;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class UserRouteHandler implements RouteHandler {

    private static final String BASE = "/api/users";

    private final UserController controller;

    public UserRouteHandler(UserController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            HttpMethod method = HttpMethod.from(exchange.getRequestMethod());

            if (path.equals(BASE + "/register")) {
                if (method == HttpMethod.POST) {
                    controller.register(exchange);
                } else {
                    ErrorResponses.methodNotAllowed(exchange, "POST");
                }
                return;
            }

            if (path.equals(BASE + "/login")) {
                if (method == HttpMethod.POST) {
                    controller.login(exchange);
                } else {
                    ErrorResponses.methodNotAllowed(exchange, "POST");
                }
                return;
            }

            if (path.matches(BASE + "/[1-9][0-9]*/profile")) {
                String idText = path.substring(BASE.length() + 1, path.length() - "/profile".length());
                int id = Integer.parseInt(idText);
                switch (method == null ? HttpMethod.POST : method) {
                    case GET -> controller.getProfile(exchange, id);
                    case PUT -> controller.updateProfile(exchange, id);
                    default -> ErrorResponses.methodNotAllowed(exchange, "GET, PUT");
                }
                return;
            }

            ErrorResponses.send(exchange, 404, "Route not found");
        } catch (Exception e) {
            ErrorResponses.send(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
