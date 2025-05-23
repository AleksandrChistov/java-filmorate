package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dal.dto.UpdateReviewDto;
import ru.yandex.practicum.filmorate.model.Review;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewMapper {

    public static Review updateReview(Review oldReview, UpdateReviewDto newReview) {
        oldReview.setContent(newReview.getContent());
        oldReview.setIsPositive(newReview.isPositive());

        return oldReview;
    }
}
