package at.fhtw.mrp.repository;

import at.fhtw.mrp.model.Media;

import java.util.List;

public interface MediaRepository extends Repository<Media, Integer>{
    List<Media> search(MediaFilter filter);
}
