package at.fhtw.mrp.server;

import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.service.JwtService;
import at.fhtw.mrp.service.MediaService;
import at.fhtw.mrp.service.RatingService;
import at.fhtw.mrp.service.UserService;
import at.fhtw.mrp.service.JwtService;
import com.sun.net.httpserver.HttpServer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;


@RequiredArgsConstructor

//SERVER startet den Server und verarbeitet die Http Anfrage
public class HttpServerManager {




    public void start() throws IOException {

        // 1. SERVICES ERSTELLEN
        RatingService ratingService = new RatingService();

        MediaService mediaService =
                new MediaService(ratingService);

        JwtService jwtService =
                new JwtService();



        UserService userService = new UserService(
                ratingService,
                mediaService
        );

        UserHandler userHandler = new UserHandler(
                userService,
                jwtService
        );


        //erstelle einen HTTP Server
        //8080 ist der Port Anfragen, auf dem Anfragen empfandgen werden
        //0 bedeutet: Standardgrösse fuer die Warteschlange
        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0
        );

        //Anfrage wird verarbeitet
        server.createContext(
                "/api/media",
                new MediaHandler(mediaService)
        );

        server.createContext(
                "/api/users",
                userHandler
        );

        //Server startet
        server.start();
        System.out.println("Server gestartet");
    }
}