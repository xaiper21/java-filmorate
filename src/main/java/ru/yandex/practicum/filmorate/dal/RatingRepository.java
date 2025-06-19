package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.catsgram.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dto.dtoclasses.RatingDto;

import java.util.List;
import java.util.Optional;

@Repository
public class RatingRepository extends BaseRepository<RatingDto> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM rating";

    public RatingRepository(JdbcTemplate jdbc, RowMapper<RatingDto> mapper) {
        super(jdbc, mapper);
    }

    public Optional<RatingDto> findOne(long id) {
        return super.findOne(FIND_BY_ID_QUERY, id);
    }

    public List<RatingDto> findMany() {
        return super.findMany(FIND_ALL_QUERY);
    }
}
