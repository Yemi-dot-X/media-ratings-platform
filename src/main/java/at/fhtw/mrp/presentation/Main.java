package at.fhtw.mrp.presentation;
import at.fhtw.mrp.server.Server;


import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Server server = new Server(8080);

        try {
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
