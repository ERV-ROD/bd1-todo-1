package tec.bd.social.repository;

import tec.bd.social.Rating;
import tec.bd.social.Review;
import tec.bd.social.datasource.DBManager;

import java.sql.Date;
import java.sql.ResultSet;
import tec.bd.social.datasource.DBManager;
import tec.bd.social.service.RatingsServiceImpl;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;

public class ReviewsRepositoryImpl extends BaseRepository<Review> implements ReviewsRepository{

    private static final String GET_ALL_REVIEWS = "{call get_all_reviews(?)}";
    private static final String ADD_REVIEW = "{call add_review(?,?,?,?)}";
    private static final String DELETE_REVIEW = "{call delete_review(?,?)}";
    private static final String UPDATE_REVIEW = "{call update_review(?,?,?,?)}";

    public ReviewsRepositoryImpl(DBManager dbManager){
        super(dbManager);
    }

    @Override
    public ArrayList<Review> getReviews(String todoId) {

        try {
            var connection = this.connect();
            ArrayList<Review> reviews = new ArrayList<Review>();
            var statement = connection.prepareCall(GET_ALL_REVIEWS);
            statement.setString(1, todoId);
            var resultSet = this.query(statement);
            while(resultSet.next()) {
                reviews.add(toEntity(resultSet));
            }
            return reviews;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Review addNewReview(String reviewText, String clientId, String todoId, Date createdAt) {
        try {
            var connection = this.connect();
            var statement = connection.prepareCall(ADD_REVIEW);
            statement.setString(1, clientId);
            statement.setString(2, todoId);
            statement.setString(3, reviewText);
            statement.setDate(4, (java.sql.Date) createdAt);
            var resultSet = this.query(statement);

            var review =toEntity(resultSet);
            return review;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String deleteReview(String clientId, String todoId) {
        try {
            var connection = this.connect();
            CallableStatement statement = connection.prepareCall(DELETE_REVIEW);
            statement.setString(1,clientId);
            statement.setString(2,todoId);
            var resultSet = this.query(statement);
            while(resultSet.next()) {
                var message = resultSet.getString("status");
                return message;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Falló el proceso";
    }

    @Override
    public Review updateReview(String clientId, String todoId, String reviewText, Date createdAt) {
        try {
            var connection = this.connect();
            CallableStatement statement = connection.prepareCall(UPDATE_REVIEW);
            statement.setString(1, clientId);
            statement.setString(2, todoId);
            statement.setString(3, reviewText);
            statement.setDate(4, (java.sql.Date) createdAt);
            var resultSet = this.query(statement);
            while(resultSet.next()) {
                var review = toEntity(resultSet);
                return review;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Review toEntity(ResultSet resultSet) {
        try {
            var review = new Review(
                    resultSet.getInt("id"),
                    resultSet.getString("reviewText"),
                    resultSet.getString("todoId"),
                    resultSet.getString("clientId"),
                    resultSet.getDate("createdAt")
            );
            return review;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
