package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.dtoclasses.MpaWithIdAndName;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<MpaWithIdAndName> {
    private static final String FIND_BY_ID_QUERY = "SELECT id AS mpa_id, name AS mpa_name FROM rating WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id AS mpa_id, name AS mpa_name FROM rating ORDER BY id ASC";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<MpaWithIdAndName> mapper) {
        super(jdbc, mapper);
    }

    public Optional<MpaWithIdAndName> findOne(int id) {
        return super.findOne(FIND_BY_ID_QUERY, id);
    }

    public List<MpaWithIdAndName> findMany() {
        return super.findMany(FIND_ALL_QUERY);
    }
}
