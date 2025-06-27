package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.dtoclasses.MpaWithIdAndName;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMapper implements RowMapper<MpaWithIdAndName> {
    @Override
    public MpaWithIdAndName mapRow(ResultSet rs, int rowNum) throws SQLException {
        MpaWithIdAndName mpa = new MpaWithIdAndName();
        mpa.setId(rs.getInt("mpa_id"));
        mpa.setName(rs.getString("mpa_name"));
        return mpa;
    }
}
