package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.catsgram.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dto.dtoclasses.GenreDto;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<GenreDto> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<GenreDto> mapper) {
        super(jdbc, mapper);
    }

    public Optional<GenreDto> findOne(long id) {
        return super.findOne(FIND_BY_ID_QUERY, id);
    }

    public List<GenreDto> findMany() {
        return super.findMany(FIND_ALL_QUERY);
    }
}
