//Main Erstellt die Objekte
package at.fhtw.mrp.presentation;

import at.fhtw.mrp.server.HttpServerManager;
import at.fhtw.mrp.server.MediaHandler;
import at.fhtw.mrp.server.UserHandler;
import at.fhtw.mrp.service.MediaService;
import at.fhtw.mrp.service.RatingService;
import at.fhtw.mrp.service.UserService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {

        //RatingService erstellen
        RatingService ratingService = new RatingService();
        //RatingService an MediaService
        MediaService mediaService = new MediaService(ratingService);
        UserService userService = new UserService(
                ratingService,
                mediaService
        );

        // 2. HTTP SERVER ERSTELLEN
        //ServerMangaer Objekt erstellen
        //Dependency Injection hiermit erstelle ich die benötige Verbindung
        //über den Konstruktor
        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0
        );

        // 3. HANDLER REGISTRIEREN
        server.createContext(
                "/api/media",
                new MediaHandler(mediaService)
        );

        server.createContext(
                "/api/user",
                new UserHandler(userService)
        );

        // 4. SERVER STARTEN
        server.start();

        System.out.println("Server gestartet");
    }
}

