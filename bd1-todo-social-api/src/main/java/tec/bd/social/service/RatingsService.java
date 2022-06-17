package tec.bd.social.service;

import tec.bd.social.Rating;

import java.sql.Date;

public interface RatingsService {

    Rating getRating(int ratingId);

    float getRatingAverage(String todoId);

    Rating addNewRating(String clientId, String todoId, Date date, int rate);

    String deleteTodo(String clientId, String todoId);
}
