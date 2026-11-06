package at.fhtw.mrp.server;


import at.fhtw.mrp.controller.Controller;
import at.fhtw.mrp.controller.MediaController;
import at.fhtw.mrp.controller.UserController;
import at.fhtw.mrp.database.DatabaseInitializer;
import at.fhtw.mrp.repository.JdbcMediaRepository;
import at.fhtw.mrp.repository.JdbcUserRepository;
import at.fhtw.mrp.repository.MediaRepository;
import at.fhtw.mrp.repository.UserRepository;
import at.fhtw.mrp.service.JwtService;
import at.fhtw.mrp.service.MediaService;
import at.fhtw.mrp.service.RatingService;
import at.fhtw.mrp.service.UserService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;

// Startet den HTTP Server und verbindet die Schichten:
//Repository -> Service -> Controller -> RouteHandler.

public class Server {

    private final int port;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final boolean initDatabase;
    private HttpServer server;

    /** Normaler Start mit PostgreSQL. Die Tabellen werden beim Start angelegt. */
    public Server(int port) {
        this(port, new JdbcUserRepository(), new JdbcMediaRepository(), true);
    }

    /** Start mit eigenen Repositories, zum Beispiel In-Memory für Tests. */
    public Server(int port, UserRepository userRepository, MediaRepository mediaRepository) {
        this(port, userRepository, mediaRepository, false);
    }

    private Server(int port, UserRepository userRepository,
                   MediaRepository mediaRepository, boolean initDatabase) {
        this.port = port;
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
        this.initDatabase = initDatabase;
    }

    public void start() throws IOException {
        if (server != null) {
            throw new IllegalStateException("Server is already started");
        }

        if (initDatabase) {
            DatabaseInitializer.createTables();
        }

        // 1. Services
        JwtService jwtService = new JwtService();
        RatingService ratingService = new RatingService();
        MediaService mediaService = new MediaService(mediaRepository);
        UserService userService = new UserService(ratingService, mediaService, userRepository);

        // 2. Controller
        Controller mediaController = new MediaController(mediaService, jwtService);
        UserController userController = new UserController(userService, jwtService);

        // 3. RouteHandler
        RouteHandler mediaRouteHandler = new MediaRouteHandler(mediaController);
        RouteHandler userRouteHandler = new UserRouteHandler(userController);

        // Port 0 wählt einen freien Port. Das braucht man für Tests.
        server = HttpServer.create(new InetSocketAddress(port), 10);
        server.createContext("/api/media", mediaRouteHandler);
        server.createContext("/api/users", userRouteHandler);
        server.start();

        System.out.println("MRP server started at http://localhost:" + getPort());
    }

    public int getPort() {
        return server == null ? port : server.getAddress().getPort();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
    }
}

