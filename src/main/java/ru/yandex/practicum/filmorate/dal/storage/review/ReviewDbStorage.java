package ru.yandex.practicum.filmorate.dal.storage.review;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewDbStorage extends BaseDbStorage implements ReviewStorage {
    private static final String FIND_ALL_REVIEWS_QUERY = "SELECT * FROM reviews ORDER BY useful DESC";
    private static final String FIND_ALL_REVIEWS_BY_FILM_ID_AND_COUNT_QUERY = "SELECT * FROM reviews " +
            "WHERE film_id = ? " +
            "ORDER BY useful DESC " +
            "LIMIT ?";
    private static final String FIND_ALL_REVIEWS_BY_COUNT_QUERY = "SELECT * FROM reviews ORDER BY useful DESC LIMIT ?";
    private static final String FIND_REVIEW_BY_ID_QUERY = "SELECT * FROM reviews WHERE id = ?";

    private static final String INSERT_REVIEW_QUERY = "INSERT INTO reviews (content, is_positive, user_id, film_id, useful) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_REVIEW_QUERY = "UPDATE reviews SET content = ?, is_positive = ? WHERE id = ?";
    private static final String UPDATE_USEFUL_OF_REVIEW_QUERY = "UPDATE reviews SET useful = ? WHERE id = ?";

    private static final String DELETE_REVIEW_BY_ID_QUERY = "DELETE FROM reviews WHERE id = ?";

    private static final String GET_EVENT_BY_REVIEW_ID_AND_USER_ID =
            "SELECT is_like FROM reviews_likes WHERE review_id = ? AND user_id = ?";

    private static final String INSERT_LIKE_TO_REVIEW_QUERY = "INSERT INTO reviews_likes (review_id, user_id, is_like) VALUES (?, ?, ?)";

    private final RowMapper<Review> mapper;
    private final RowMapper<Boolean> reviewsLikesMapper;

    public ReviewDbStorage(JdbcTemplate jdbc, RowMapper<Review> mapper, RowMapper<Boolean> reviewsLikesMapper) {
        super(jdbc);
        this.mapper = mapper;
        this.reviewsLikesMapper = reviewsLikesMapper;
    }

    @Override
    public List<Review> getAll(long filmId, int count) {
        return findMany(FIND_ALL_REVIEWS_BY_FILM_ID_AND_COUNT_QUERY, mapper, filmId, count);
    }

    @Override
    public List<Review> getAll(int count) {
        return findMany(FIND_ALL_REVIEWS_BY_COUNT_QUERY, mapper, count);
    }

    @Override
    public List<Review> getAll() {
        return findMany(FIND_ALL_REVIEWS_QUERY, mapper);
    }

    @Override
    public Optional<Review> getById(long reviewId) {
        return findOne(FIND_REVIEW_BY_ID_QUERY, mapper, reviewId);
    }

    @Override
    public Optional<Boolean> getMadeLikeByReviewIdAndUserId(long reviewId, long userId) {
        return findOne(GET_EVENT_BY_REVIEW_ID_AND_USER_ID, reviewsLikesMapper, reviewId, userId);
    }

    @Override
    public Review add(Review newReview) {
        long id = insert(
                INSERT_REVIEW_QUERY,
                newReview.getContent(),
                newReview.getIsPositive(),
                newReview.getUserId(),
                newReview.getFilmId(),
                0
        );
        newReview.setReviewId(id);
        return newReview;
    }

    @Override
    public Boolean addLikeToReview(long reviewId, long userId, boolean isLIke) {
        long id = insert(
                INSERT_LIKE_TO_REVIEW_QUERY,
                reviewId,
                userId,
                isLIke
        );

        return id > 0;
    }

    @Override
    public Review update(Review updatedReview) {
        update(
                UPDATE_REVIEW_QUERY,
                updatedReview.getContent(),
                updatedReview.getIsPositive(),
                updatedReview.getReviewId()
        );
        return updatedReview;
    }

    @Override
    public boolean delete(long reviewId) {
        return delete(DELETE_REVIEW_BY_ID_QUERY, reviewId);
    }

    @Override
    public void updateRating(long reviewId, int newRating) {
        update(
                UPDATE_USEFUL_OF_REVIEW_QUERY,
                newRating,
                reviewId
        );
    }
}
