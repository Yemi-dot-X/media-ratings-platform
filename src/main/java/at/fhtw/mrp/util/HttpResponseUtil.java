package at.fhtw.mrp.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpResponseUtil {
    public static void sendText(
            HttpExchange exchange,
            int statusCode,
            String message) throws IOException {

        // Antwort als normalen Text kennzeichnen

        byte[] response = message.getBytes(
                StandardCharsets.UTF_8
        );
        // HTTP-Status und Länge senden

        exchange.getResponseHeaders().set(
                "Content-Type",
                "text/plain; charset=UTF-8"
        );
        // Antwort schreiben und Stream schließen

        exchange.sendResponseHeaders(
                statusCode, response.length
        );

        try (OutputStream output =
                     exchange.getResponseBody()) {
            output.write(response);
        }
    }

    public static void sendJson(
            HttpExchange exchange,
            int statusCode,
            Object body) throws IOException {

        byte[] response = Json.MAPPER.writeValueAsBytes(body);

        exchange.getResponseHeaders().set(
                "Content-Type", "application/json; charset=UTF-8"
        );

        exchange.sendResponseHeaders(statusCode, response.length);

        try (OutputStream output = exchange.getResponseBody()) {
            output.write(response);
        }
    }
}
