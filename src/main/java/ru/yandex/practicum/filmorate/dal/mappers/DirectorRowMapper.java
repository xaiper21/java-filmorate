package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.dtoclasses.DirectorDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DirectorRowMapper implements RowMapper<DirectorDto> {
    @Override
    public DirectorDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        DirectorDto director = new DirectorDto();
        director.setId(rs.getLong("id"));
        director.setName(rs.getString("name"));
        return director;
    }
}