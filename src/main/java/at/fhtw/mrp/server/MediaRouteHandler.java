package at.fhtw.mrp.server;



 // Routen:
  //api/media        GET (Liste mit Filter), POST
 //api/media/{id}   GET, PUT, DELETE

import at.fhtw.mrp.controller.Controller;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class MediaRouteHandler implements RouteHandler {

    private static final String BASE = "/api/media";

    private final Controller controller;

    public MediaRouteHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            HttpMethod method = HttpMethod.from(exchange.getRequestMethod());

            if (path.equals(BASE) || path.equals(BASE + "/")) {
                switch (method == null ? HttpMethod.PUT : method) {
                    case GET -> controller.getAll(exchange);
                    case POST -> controller.post(exchange);
                    default -> ErrorResponses.methodNotAllowed(exchange, "GET, POST");
                }
                return;
            }

            if (path.matches(BASE + "/[1-9][0-9]*")) {
                int id = Integer.parseInt(path.substring(BASE.length() + 1));
                switch (method == null ? HttpMethod.POST : method) {
                    case GET -> controller.get(exchange, id);
                    case PUT -> controller.put(exchange, id);
                    case DELETE -> controller.delete(exchange, id);
                    default -> ErrorResponses.methodNotAllowed(exchange, "GET, PUT, DELETE");
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
