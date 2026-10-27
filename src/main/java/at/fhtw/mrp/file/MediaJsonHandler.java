
package at.fhtw.mrp.file;

import at.fhtw.mrp.model.Media;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MediaJsonHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    // EXPORT
    public void saveMedia(List<Media> media, String filename)
            throws IOException {

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(filename), media);
    }

    // IMPORT
    public List<Media> loadMedia(String filename)
            throws IOException {

        return mapper.readValue(
                new File(filename),
                new TypeReference<List<Media>>() {}
        );
    }
}
