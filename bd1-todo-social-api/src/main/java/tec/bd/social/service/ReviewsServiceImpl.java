package tec.bd.social.service;

import tec.bd.social.Review;
import tec.bd.social.repository.RatingsRepository;
import tec.bd.social.repository.ReviewsRepository;

import java.sql.Date;
import java.util.ArrayList;

public class ReviewsServiceImpl implements ReviewsService{
    private ReviewsRepository reviewsRepository;

    public ReviewsServiceImpl(ReviewsRepository reviewsRepository){
        this.reviewsRepository = reviewsRepository;
    }



    @Override
    public ArrayList<Review> getReviews(String todoId) {
        return this.reviewsRepository.getReviews(todoId);
    }

    @Override
    public Review addNewReview(String reviewText, String clientId,String todoId, Date createdAt) {
        return this.reviewsRepository.addNewReview(reviewText,clientId,todoId,createdAt);
    }

    @Override
    public String deleteReview(String clientId, String todoId) {
        return this.reviewsRepository.deleteReview(clientId,todoId);
    }

    @Override
    public Review updateReview(String clientId, String todoId, String reviewText, Date createAt) {
        return this.reviewsRepository.updateReview(clientId,todoId,reviewText,createAt);
    }
}
