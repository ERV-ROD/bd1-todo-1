package tec.bd.social.service;

import tec.bd.social.Review;

import java.sql.Date;
import java.util.ArrayList;

public interface ReviewsService {

    ArrayList<Review> getReviews(String todoId);

    Review addNewReview(String reviewText, String clientId,String todoId, Date createdAt);

    String deleteReview(String clientId, String todoId);

    Review updateReview(String clientId, String todoId, String reviewText,Date createdAt);
}
