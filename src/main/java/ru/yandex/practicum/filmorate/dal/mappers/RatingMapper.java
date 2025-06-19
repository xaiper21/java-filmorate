package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.dtoclasses.RatingDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingMapper implements RowMapper<RatingDto> {
    @Override
    public RatingDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        RatingDto ratingDto = new RatingDto();
        ratingDto.setId(rs.getInt("id"));
        ratingDto.setName(rs.getString("name"));
        return ratingDto;
    }
}
