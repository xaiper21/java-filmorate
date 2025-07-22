package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.ReviewLike;

import java.util.Optional;

@Repository
public class ReviewLikeRepository {
    private static final String INSERT_QUERY = "INSERT INTO review_likes (review_id, user_id, is_like) VALUES (?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM review_likes WHERE review_id=? AND user_id=?";
    private static final String FIND_BY_REVIEW_AND_USER_QUERY = "SELECT * FROM review_likes WHERE review_id=? AND user_id=?";
    private static final String UPDATE_QUERY = "UPDATE review_likes SET is_like=? WHERE review_id=? AND user_id=?";
    private static final String COUNT_LIKES_QUERY = "SELECT COUNT(*) FROM review_likes WHERE review_id=? AND is_like=true";
    private static final String COUNT_DISLIKES_QUERY = "SELECT COUNT(*) FROM review_likes WHERE review_id=? AND is_like=false";
    private final JdbcTemplate jdbc;
    private final RowMapper<ReviewLike> mapper;

    public ReviewLikeRepository(JdbcTemplate jdbc, RowMapper<ReviewLike> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public void addLike(ReviewLike reviewLike) {
        jdbc.update(INSERT_QUERY,
                reviewLike.getReviewId(),
                reviewLike.getUserId(),
                reviewLike.getIsLike());
    }

    public void removeLike(long reviewId, long userId) {
        jdbc.update(DELETE_QUERY, reviewId, userId);
    }

    public Optional<ReviewLike> findByReviewAndUser(long reviewId, long userId) {
        return jdbc.query(FIND_BY_REVIEW_AND_USER_QUERY, mapper, reviewId, userId).stream().findFirst();
    }

    public void updateLike(ReviewLike reviewLike) {
        jdbc.update(UPDATE_QUERY,
                reviewLike.getIsLike(),
                reviewLike.getReviewId(),
                reviewLike.getUserId());
    }

    public int countLikes(long reviewId) {
        Integer count = jdbc.queryForObject(COUNT_LIKES_QUERY, Integer.class, reviewId);
        return count != null ? count : 0;
    }

    public int countDislikes(long reviewId) {
        Integer count = jdbc.queryForObject(COUNT_DISLIKES_QUERY, Integer.class, reviewId);
        return count != null ? count : 0;
    }
}