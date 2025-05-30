package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.UpdateReviewDto;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.annotation.Nullable;
import java.util.List;

@RestController
@RequestMapping(ReviewController.URL)
@RequiredArgsConstructor
@Validated
public class ReviewController {
    private final ReviewService reviewService;

    public static final String URL = "/reviews";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Review> findAll(@RequestParam @Nullable Long filmId, @RequestParam(defaultValue = "10") int count) {
        return reviewService.getAll(filmId, count);
    }

    @GetMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public Review findById(@PathVariable @Positive Long reviewId) {
        return reviewService.getById(reviewId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review create(@Validated @RequestBody Review newReview) {
        return reviewService.create(newReview);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Review update(@Validated @RequestBody UpdateReviewDto newReview) {
        return reviewService.update(newReview);
    }


    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long reviewId) {
        reviewService.delete(reviewId);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable @Positive Long reviewId, @PathVariable @Positive Long userId) {
        reviewService.addLike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable @Positive Long reviewId, @PathVariable @Positive Long userId) {
        reviewService.deleteLike(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addDislike(@PathVariable @Positive Long reviewId, @PathVariable @Positive Long userId) {
        reviewService.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDislike(@PathVariable @Positive Long reviewId, @PathVariable @Positive Long userId) {
        reviewService.deleteDislike(reviewId, userId);
    }

}
