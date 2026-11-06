package at.fhtw.mrp.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtil {

    private HttpRequestUtil() {
    }

    /** Liest den JSON Body. Ein leerer Body führt zu einer JsonProcessingException (400). */
    public static <T> T readBody(HttpExchange exchange, Class<T> type) throws IOException {
        T value = Json.MAPPER.readValue(exchange.getRequestBody(), type);
        if (value == null) {
            throw new IllegalArgumentException("Request body is empty");
        }
        return value;
    }

    /** Zerlegt ?a=1&b=2 in eine Map. Leere Werte werden weggelassen. */
    public static Map<String, String> queryParams(URI uri) {
        Map<String, String> params = new HashMap<>();
        String query = uri.getRawQuery();
        if (query == null || query.isBlank()) {
            return params;
        }
        for (String pair : query.split("&")) {
            int index = pair.indexOf('=');
            String key = index < 0 ? pair : pair.substring(0, index);
            String value = index < 0 ? "" : pair.substring(index + 1);
            key = URLDecoder.decode(key, StandardCharsets.UTF_8);
            value = URLDecoder.decode(value, StandardCharsets.UTF_8);
            if (!key.isBlank() && !value.isBlank()) {
                params.put(key, value);
            }
        }
        return params;
    }
}
