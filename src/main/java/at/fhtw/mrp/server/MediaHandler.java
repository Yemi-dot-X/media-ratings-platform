package at.fhtw.mrp.server;


/*
server/Server.java        -> startet den HTTP-Server
server/MediaHandler.java  -> verarbeitet /api/media Requests
service/MediaService.java -> eigentliche CRUD-Logik
model/Media.java          -> Datenmodell

 */
import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.service.MediaService;
import at.fhtw.mrp.dto.UpdateMediaRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.RequiredArgsConstructor;
import static at.fhtw.mrp.util.HttpResponseUtil.sendText;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
@RequiredArgsConstructor


public class MediaHandler implements HttpHandler {
    private final ObjectMapper mapper = new ObjectMapper();



    private final MediaService mediaService;


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                handleGet(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
            case  "PUT":
                handlePut(exchange);
                break;
            case "DELETE":
                handleDelete(exchange);
                break;
                
            default:
                sendText(exchange, 405, "Methode Not Allowed");
              }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        //URL Pfad der Anfrage auslesen.
        String path = exchange.getRequestURI().getPath();
        //Variable für die Antwort
        String response = "";

        if(path.equals("/api/media")){
            response = mediaService.viewMedia().toString();

        } else if(path.startsWith("/api/media/")){
            //Hiermit extrahiere ich die ID
            String idText = path.substring("/api/media/".length());

            try{
                //String ID umwandeln in GanzZahl
                int id = Integer.parseInt(idText);
                //Id suchen
                Media media = mediaService.findById(id);
                if(media == null){
                    sendText(exchange, 404, "Media not found");
                    return;
                }

                response = mapper.writeValueAsString(media);
            } catch (NumberFormatException e) {
                sendText(exchange, 400, "Invalid media ID");
                return;
            }
        } else {
            sendText(exchange, 404, "Not Found");
            return;
        }

        //Antwort umwandeln
        byte[] data = response.getBytes(StandardCharsets.UTF_8);

        //HTTP Status und Antwortänge
        exchange.sendResponseHeaders(200, data.length);

        //Antwort an Postman
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(data);
        }
    }
    //POST uebermittelt den Request Body
    private void handlePost(HttpExchange exchange) throws IOException{
        InputStream input = exchange.getRequestBody();
        ObjectMapper mapper = new ObjectMapper();

        Media media = mapper.readValue(
                exchange.getRequestBody(),
                Media.class
        );
        mediaService.createMedia(media);

        sendText(exchange, 201, "Media created");

    }



    private void handlePut(HttpExchange exchange) throws IOException {

        // 1. URL-Pfad auslesen
        String path = exchange.getRequestURI().getPath();

        // 2. Prüfen, ob der Pfad eine Media-ID enthält
        if (!path.matches("/api/media/\\d+")) {
            sendText(exchange, 400, "Invalid media path");
            return;
        }

        try {
            // 3. Media-ID aus der URL extrahieren
            String idText = path.substring("/api/media/".length());
            int mediaId = Integer.parseInt(idText);

            // 4. User-ID aus dem Query-Parameter lesen
            String query = exchange.getRequestURI().getQuery();

            if (query == null || !query.matches("userId=\\d+")) {
                sendText(exchange, 400, "Invalid user ID");
                return;
            }

            int userId = Integer.parseInt(
                    query.substring("userId=".length())
            );

            // 5. JSON-Body in UpdateMediaRequest umwandeln
            UpdateMediaRequest request = mapper.readValue(
                    exchange.getRequestBody(),
                    UpdateMediaRequest.class
            );

            // 6. MediaService aufrufen
            mediaService.updateMedia(mediaId, userId, request);

            // 7. Erfolgreiche Antwort senden
            sendText(exchange, 200, "Media updated");

        } catch (JsonProcessingException e) {
            sendText(exchange, 400, "Invalid JSON");

        } catch (NumberFormatException e) {
            sendText(exchange, 400, "Invalid ID");

        } catch (IllegalArgumentException e) {
            sendText(exchange, 400, e.getMessage());
        }
    }


    private void handleDelete(HttpExchange exchange)
            throws IOException {

        String path = exchange.getRequestURI().getPath();

        try {
            int mediaId = Integer.parseInt(
                    path.substring("/api/media/".length())
            );

            String query = exchange.getRequestURI().getQuery();

            if (query == null || !query.matches("userId=\\d+")) {
                sendText(exchange, 400, "Invalid user ID");
                return;
            }

            int userId = Integer.parseInt(
                    query.substring("userId=".length())
            );

            mediaService.deleteMedia(mediaId, userId);

            sendText(exchange, 200, "Media deleted");

        } catch (NumberFormatException e) {
            sendText(exchange, 400, "Invalid ID");
        } catch (IllegalArgumentException e) {
            sendText(exchange, 400, e.getMessage());
        }

    }
}
