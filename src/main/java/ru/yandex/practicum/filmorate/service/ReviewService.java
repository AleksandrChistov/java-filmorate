package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dto.UpdateReviewDto;
import ru.yandex.practicum.filmorate.dal.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.function.Function;

@Service
@Slf4j
public class ReviewService {
    private final ReviewStorage reviewStorage;

    public ReviewService(ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    public List<Review> getAll(Long filmId, int count) {
        if (filmId != null) {
            return reviewStorage.getAll(filmId, count);
        }
        return reviewStorage.getAll(count);
    }

    public Review getById(long reviewId) {
        return reviewStorage.getById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыв с id = " + reviewId + " не найден."));
    }

    public Review create(Review newReview) {
        log.info("Добавление нового отзыва к фильму с id = {}", newReview.getFilmId());

        if (newReview.getUserId() < 0) {
            throw new NotFoundException("Пользователь с id = " + newReview.getUserId() + " не найден.");
        }

        if (newReview.getFilmId() < 0) {
            throw new NotFoundException("Фильм с id = " + newReview.getFilmId() + " не найден.");
        }

        Review createdReview = reviewStorage.add(newReview);

        log.info("Отзыв успешно добавлен, id = {}", createdReview.getReviewId());

        return createdReview;
    }

    public Review update(UpdateReviewDto newReview) {
        log.info("Обновление отзыва с id = {}", newReview.getReviewId());

        Review updatedReview = reviewStorage.getById(newReview.getReviewId())
                .map(oldReview -> ReviewMapper.updateReview(oldReview, newReview))
                .orElseThrow(() -> new NotFoundException("Отзыв с id = " + newReview.getReviewId() + " не найден."));

        updatedReview = reviewStorage.update(updatedReview);

        log.info("Отзыв успешно обновлен, id = {}", updatedReview.getReviewId());

        return updatedReview;
    }

    public boolean delete(long reviewId) {
        log.info("Удаление отзыва с id = {}", reviewId);
        return reviewStorage.delete(reviewId);
    }

    public void addLike(long reviewId, long userId) {
        log.info("Добавление лайка к отзыву с id = {}, пользователем с id = {}", reviewId, userId);
        updateRating(reviewId, oldRating -> oldRating + 1);
        log.info("Лайк успешно добавлен");
    }

    public void addDislike(long reviewId, long userId) {
        log.info("Добавление дизлайка к отзыву с id = {}, пользователем с id = {}", reviewId, userId);
        updateRating(reviewId, oldRating -> oldRating - 1);
        log.info("Дизлайк успешно добавлен");
    }

    public void deleteLike(long reviewId, long userId) {
        log.info("Удаление лайка из отзыва с id = {}, пользователем с id = {}", reviewId, userId);
        updateRating(reviewId, oldRating -> oldRating - 1);
        log.info("Лайк успешно удален");
    }

    public void deleteDislike(long reviewId, long userId) {
        log.info("Удаление дизлайка из отзыва с id = {}, пользователем с id = {}", reviewId, userId);
        updateRating(reviewId, oldRating -> oldRating + 1);
        log.info("Дизлайк успешно удален");
    }

    private void updateRating(long reviewId, Function<Integer, Integer> getNewRating) {
        int rating = getById(reviewId).getUseful();

        reviewStorage.updateRating(reviewId, getNewRating.apply(rating));
    }

}
