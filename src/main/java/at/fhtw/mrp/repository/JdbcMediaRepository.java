package at.fhtw.mrp.repository;

import at.fhtw.mrp.database.DataAccessException;
import at.fhtw.mrp.database.UnitOfWork;
import at.fhtw.mrp.model.Genre;
import at.fhtw.mrp.model.Media;
import at.fhtw.mrp.model.MediaType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JdbcMediaRepository implements MediaRepository {


    private static final String COLUMNS =
            "id, creator_id, title, description, media_type, release_year, "
                    + "age_restriction, duration, genres";

    @Override
    public List<Media> readAll() {
        return search(new MediaFilter());
    }

    @Override
    public List<Media> search(MediaFilter filter) {
        StringBuilder sql = new StringBuilder("SELECT " + COLUMNS + " FROM media WHERE 1 = 1");
        List<Object> parameters = new ArrayList<>();

        if (filter.getTitle() != null) {
            sql.append(" AND LOWER(title) LIKE ?");
            parameters.add("%" + filter.getTitle().toLowerCase() + "%");
        }
        if (filter.getGenre() != null) {
            sql.append(" AND (',' || genres || ',') LIKE ?");
            parameters.add("%," + filter.getGenre().name() + ",%");
        }
        if (filter.getMediaType() != null) {
            sql.append(" AND media_type = ?");
            parameters.add(filter.getMediaType().name());
        }
        if (filter.getReleaseYear() != null) {
            sql.append(" AND release_year = ?");
            parameters.add(filter.getReleaseYear());
        }
        if (filter.getAgeRestriction() != null) {
            sql.append(" AND age_restriction <= ?");
            parameters.add(filter.getAgeRestriction());
        }

        if ("title".equals(filter.getSortBy())) {
            sql.append(" ORDER BY LOWER(title), id");
        } else if ("releaseYear".equals(filter.getSortBy())) {
            sql.append(" ORDER BY release_year NULLS LAST, id");
        } else {
            sql.append(" ORDER BY id");
        }

        try (UnitOfWork unitOfWork = new UnitOfWork();
             PreparedStatement statement = unitOfWork.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            List<Media> result = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(map(resultSet));
                }
            }
            unitOfWork.commitTransaction();
            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Lesen der Medien nicht erfolgreich", e);
        }
    }

    @Override
    public Media readById(Integer id) {
        try (UnitOfWork unitOfWork = new UnitOfWork();
             PreparedStatement statement = unitOfWork.prepareStatement(
                     "SELECT " + COLUMNS + " FROM media WHERE id = ?")) {
            statement.setInt(1, id);
            Media media = null;
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    media = map(resultSet);
                }
            }
            unitOfWork.commitTransaction();
            return media;
        } catch (SQLException e) {
            throw new DataAccessException("Lesen des Mediums nicht erfolgreich", e);
        }
    }

    @Override
    public Media create(Media entity) {
        try (UnitOfWork unitOfWork = new UnitOfWork();
             PreparedStatement statement = unitOfWork.prepareStatement("""
                     INSERT INTO media (creator_id, title, description, media_type,
                                        release_year, age_restriction, duration, genres)
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                     RETURNING id, creator_id, title, description, media_type,
                               release_year, age_restriction, duration, genres
                     """)) {
            statement.setInt(1, entity.getCreatorId());
            bindValues(statement, entity, 2);
            try {
                Media created;
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    created = map(resultSet);
                }
                unitOfWork.commitTransaction();
                return created;
            } catch (SQLException e) {
                unitOfWork.rollbackTransaction();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Anlegen des Mediums nicht erfolgreich", e);
        }
    }

    @Override
    public Media updateById(Integer id, Media entity) {
        try (UnitOfWork unitOfWork = new UnitOfWork();
             PreparedStatement statement = unitOfWork.prepareStatement("""
                     UPDATE media
                     SET title = ?,
                         description = ?,
                         media_type = ?,
                         release_year = ?,
                         age_restriction = ?,
                         duration = ?,
                         genres = ?
                     WHERE id = ?
                     RETURNING id, creator_id, title, description, media_type,
                               release_year, age_restriction, duration, genres
                     """)) {
            bindValues(statement, entity, 1);
            statement.setInt(8, id);
            try {
                Media updated = null;
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        updated = map(resultSet);
                    }
                }
                unitOfWork.commitTransaction();
                return updated;
            } catch (SQLException e) {
                unitOfWork.rollbackTransaction();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Aktualisieren des Mediums nicht erfolgreich", e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (UnitOfWork unitOfWork = new UnitOfWork();
             PreparedStatement statement = unitOfWork.prepareStatement(
                     "DELETE FROM media WHERE id = ?")) {
            statement.setInt(1, id);
            try {
                int affected = statement.executeUpdate();
                unitOfWork.commitTransaction();
                return affected > 0;
            } catch (SQLException e) {
                unitOfWork.rollbackTransaction();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Löschen des Mediums nicht erfolgreich", e);
        }
    }

    //Setzt title, description, media_type, release_year, age_restriction, duration, genres
     //als sieben Parameter ab der Position first.

    private void bindValues(PreparedStatement statement, Media media, int first)
            throws SQLException {
        statement.setString(first, media.getTitle());
        statement.setString(first + 1, media.getDescription());
        statement.setString(first + 2, media.getMediaType().name());
        statement.setObject(first + 3, media.getReleaseYear(), Types.INTEGER);
        statement.setObject(first + 4, media.getAgeRestriction(), Types.INTEGER);
        statement.setObject(first + 5, media.getDuration(), Types.INTEGER);
        statement.setString(first + 6, genresToText(media.getGenres()));
    }

    private Media map(ResultSet resultSet) throws SQLException {
        return new Media(
                resultSet.getInt("id"),
                resultSet.getInt("creator_id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getObject("release_year", Integer.class),
                resultSet.getObject("age_restriction", Integer.class),
                textToGenres(resultSet.getString("genres")),
                MediaType.valueOf(resultSet.getString("media_type")),
                resultSet.getObject("duration", Integer.class)
        );
    }


    private String genresToText(List<Genre> genres) {
        if (genres == null) {
            return "";
        }
        return genres.stream().map(Genre::name).collect(Collectors.joining(","));
    }

    private List<Genre> textToGenres(String text) {
        List<Genre> genres = new ArrayList<>();
        if (text != null && !text.isBlank()) {
            for (String name : text.split(",")) {
                genres.add(Genre.valueOf(name));
            }
        }
        return genres;
    }
}

