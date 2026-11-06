package at.fhtw.mrp.server;


import at.fhtw.mrp.database.DataAccessException;
import at.fhtw.mrp.exception.ForbiddenException;
import at.fhtw.mrp.exception.InvalidCredentialsException;
import at.fhtw.mrp.exception.NotFoundException;
import at.fhtw.mrp.exception.UnauthorizedException;
import at.fhtw.mrp.exception.UserAlreadyExistsException;
import at.fhtw.mrp.exception.UserNotFoundException;
import at.fhtw.mrp.util.HttpResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;
// Wandelt Exceptions in HTTP Antworten um. Beide RouteHandler benutzen diese Klasse.

final class ErrorResponses {

        private ErrorResponses() {
        }

        static void send(HttpExchange exchange, Exception e)
                throws IOException {

            switch (e) {

                case UserAlreadyExistsException ex ->
                        send(exchange, 409, ex.getMessage());

                case InvalidCredentialsException ex ->
                        send(exchange, 401, "Invalid credentials");

                case UnauthorizedException ex ->
                        send(exchange, 401, ex.getMessage());

                case ForbiddenException ex ->
                        send(exchange, 403, ex.getMessage());

                case NotFoundException ex ->
                        send(exchange, 404, ex.getMessage());

                case UserNotFoundException ex ->
                        send(exchange, 404, ex.getMessage());

                case JsonProcessingException ex ->
                        send(exchange, 400, "Invalid JSON body");

                case NumberFormatException ex ->
                        send(exchange, 400, "Invalid ID");

                case IllegalArgumentException ex ->
                        send(exchange, 400, ex.getMessage());

                case DataAccessException ex -> {
                    ex.printStackTrace();
                    send(exchange, 500, "Database error");
                }

                default -> {
                    e.printStackTrace();
                    send(exchange, 500, "Internal server error");
                }
            }
        }


    static void send(HttpExchange exchange, int status, String message) throws IOException {
        HttpResponseUtil.sendJson(exchange, status, Map.of("error", message));
    }

    static void methodNotAllowed(HttpExchange exchange, String allowed) throws IOException {
        exchange.getResponseHeaders().set("Allow", allowed);
        send(exchange, 405, "Method not allowed");
    }
}
