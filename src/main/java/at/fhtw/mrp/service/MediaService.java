package at.fhtw.mrp.service;

import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Media;

import java.util.ArrayList;
import java.util.List;

public class MediaService {

        private List<Media> mediaList = new ArrayList<>();


        public void createMedia(Media media) {
            mediaList.add(media);
        }


        public List<Media> viewMedia() {
            return mediaList;
        }


        public Media findMediaById(int id) {

            for (Media media : mediaList) {

                if (media.getId() == id) {
                    return media;
                }
            }

            return null;
        }


        public void deleteMedia(int id) {

            mediaList.removeIf(media -> media.getId() == id);
        }


        public List<Media> findByGenre(Genre genre) {

            List<Media> result = new ArrayList<>();

            for (Media media : mediaList) {

                if (media.getGenres().contains(genre)) {
                    result.add(media);
                }
            }

            return result;
        }
    }

