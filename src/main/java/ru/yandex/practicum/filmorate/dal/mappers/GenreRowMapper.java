package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.dtoclasses.GenreWithIdAndName;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreRowMapper implements RowMapper<GenreWithIdAndName> {
    @Override
    public GenreWithIdAndName mapRow(ResultSet rs, int rowNum) throws SQLException {
        GenreWithIdAndName genre = new GenreWithIdAndName();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_name"));
        return genre;
    }
}
