package at.fhtw.mrp.controller;

import at.fhtw.mrp.exception.NotFoundException;
import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.model.MediaType;
import at.fhtw.mrp.repository.MediaFilter;
import at.fhtw.mrp.service.JwtService;
import at.fhtw.mrp.service.MediaService;
import at.fhtw.mrp.util.HttpRequestUtil;
import at.fhtw.mrp.util.HttpResponseUtil;
import com.sun.net.httpserver.HttpExchange;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;
@RequiredArgsConstructor

public class MediaController implements Controller{  private final MediaService mediaService;
    private final JwtService jwtService;

    @Override
    public void getAll(HttpExchange exchange) throws IOException {
        MediaFilter filter = readFilter(HttpRequestUtil.queryParams(exchange.getRequestURI()));
        HttpResponseUtil.sendJson(exchange, 200, mediaService.search(filter));
    }

    @Override
    public void get(HttpExchange exchange, int id) throws IOException {
        Media media = mediaService.findById(id);
        if (media == null) {
            throw new NotFoundException("Media not found");
        }
        HttpResponseUtil.sendJson(exchange, 200, media);
    }

    @Override
    public void post(HttpExchange exchange) throws IOException {
        int userId = authenticate(exchange);
        Media media = HttpRequestUtil.readBody(exchange, Media.class);

        Media created = mediaService.create(media, userId);

        exchange.getResponseHeaders().set("Location", "/api/media/" + created.getId());
        HttpResponseUtil.sendJson(exchange, 201, created);
    }

    @Override
    public void put(HttpExchange exchange, int id) throws IOException {
        int userId = authenticate(exchange);
        Media changes = HttpRequestUtil.readBody(exchange, Media.class);

        Media updated = mediaService.update(id, userId, changes);

        HttpResponseUtil.sendJson(exchange, 200, updated);
    }

    @Override
    public void delete(HttpExchange exchange, int id) throws IOException {
        int userId = authenticate(exchange);

        mediaService.delete(id, userId);

        HttpResponseUtil.sendEmpty(exchange, 204);
    }

    private int authenticate(HttpExchange exchange) {
        return jwtService.requireUserId(exchange.getRequestHeaders().getFirst("Authorization"));
    }

    //Baut den Filter aus den Query Parametern. Ungültige Werte führen zu 400.
    private MediaFilter readFilter(Map<String, String> params) {
        MediaFilter filter = new MediaFilter();
        filter.setTitle(params.get("title"));
        filter.setGenre(Genre.from(params.get("genre")));
        filter.setMediaType(MediaType.from(params.get("mediaType")));
        filter.setReleaseYear(toInteger(params, "releaseYear"));
        filter.setAgeRestriction(toInteger(params, "ageRestriction"));
        filter.setSortBy(params.get("sortBy"));
        return filter;
    }

    private Integer toInteger(Map<String, String> params, String key) {
        String value = params.get(key);
        if (value == null) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(key + " must be a number");
        }
    }
}


