package ru.yandex.practicum.filmorate.dal.storage.review;

import ru.yandex.practicum.filmorate.dal.storage.Storage;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage extends Storage<Review> {
    List<Review> getAll(long filmId, int count);

    List<Review> getAll(int count);

    Optional<Review> getById(long reviewId);

    Optional<Boolean> getMadeLikeByReviewIdAndUserId(long reviewId, long userId);

    Review add(Review review);

    Boolean addLikeToReview(long reviewId, long userId, boolean isLIke);

    Review update(Review review);

    boolean delete(long reviewId);

    void updateRating(long reviewId, int newRating);
}
