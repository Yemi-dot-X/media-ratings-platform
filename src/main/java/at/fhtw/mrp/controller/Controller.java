package at.fhtw.mrp.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;


public interface Controller {

    void get(HttpExchange exchange, int id) throws IOException;

    void getAll(HttpExchange exchange) throws IOException;

    void post(HttpExchange exchange) throws IOException;

    void put(HttpExchange exchange, int id) throws IOException;

    void delete(HttpExchange exchange, int id) throws IOException;
}
