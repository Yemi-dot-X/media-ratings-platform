package at.fhtw.mrp.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public interface RouteHandler extends HttpHandler {
    @Override
    void handle(HttpExchange exchange) throws IOException;
}
