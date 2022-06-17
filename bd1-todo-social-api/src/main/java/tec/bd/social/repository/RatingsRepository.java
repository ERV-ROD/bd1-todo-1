package tec.bd.social.repository;

import tec.bd.social.Rating;

import java.sql.Date;


public interface RatingsRepository {

    Rating findById( int id);

    float findAverage();

    Rating addRating(String clientId, String todoId, Date date, int rate);

    String deleteTodo(String clientId, String todoId);
}
