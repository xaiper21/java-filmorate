package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepository extends BaseRepository<Review> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM reviews ORDER BY useful DESC";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM reviews WHERE id=?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM reviews WHERE id=?";
    private static final String INSERT_QUERY = "INSERT INTO reviews (content, is_positive, user_id, film_id, useful) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE reviews SET content=?, is_positive=? WHERE id=?";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT * FROM reviews WHERE film_id=? ORDER BY useful DESC";
    private static final String FIND_BY_USER_ID_QUERY = "SELECT * FROM reviews WHERE user_id=?";
    private static final String UPDATE_USEFUL_RATING_QUERY = "UPDATE reviews SET useful=? WHERE id=?";

    public ReviewRepository(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Review> findOne(long id) {
        return super.findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Review> findMany() {
        return super.findMany(FIND_ALL_QUERY);
    }

    public boolean delete(long id) {
        return super.delete(DELETE_BY_ID_QUERY, id);
    }

    public Review save(Review review) {
        long id = super.insert(INSERT_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getUseful()
        );
        review.setReviewId(id);
        return review;
    }

    public Review update(Review review) {
        super.update(UPDATE_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId()
        );
        return review;
    }

    public Collection<Review> findByFilmId(long filmId) {
        return super.findMany(FIND_BY_FILM_ID_QUERY, filmId);
    }

    public Collection<Review> findByUserId(long userId) {
        return super.findMany(FIND_BY_USER_ID_QUERY, userId);
    }

    public void updateUsefulRating(long reviewId, int usefulRating) {
        super.update(UPDATE_USEFUL_RATING_QUERY, usefulRating, reviewId);
    }
}