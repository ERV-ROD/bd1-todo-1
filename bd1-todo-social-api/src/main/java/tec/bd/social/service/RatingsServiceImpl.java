package tec.bd.social.service;

import tec.bd.social.Rating;
import tec.bd.social.repository.RatingsRepository;

import java.sql.Date;

public class RatingsServiceImpl implements RatingsService{

    private RatingsRepository ratingsRepository;

    public RatingsServiceImpl(RatingsRepository ratingsRepository){
        this.ratingsRepository = ratingsRepository;
    }


    @Override
    public Rating getRating(int ratingId) {
        return this.ratingsRepository.findById((ratingId));
    }

    @Override
    public float getRatingAverage(String todoId) {
        return this.ratingsRepository.findAverage();
    }

    @Override
    public Rating addNewRating(String clientId, String todoId, Date date, int rate) {
        return this.ratingsRepository.addRating(clientId,todoId,date,rate);
    }

    @Override
    public String deleteTodo(String clientId, String todoId) {
        return this.ratingsRepository.deleteTodo(clientId,todoId);
    }

}
