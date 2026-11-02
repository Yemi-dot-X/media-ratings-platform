package at.fhtw.mrp.presentation;

import at.fhtw.mrp.server.HttpServerManager;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        HttpServerManager server = new HttpServerManager();

        server.start();
    }
}