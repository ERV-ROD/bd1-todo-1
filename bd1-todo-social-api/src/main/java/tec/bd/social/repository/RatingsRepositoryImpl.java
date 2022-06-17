package tec.bd.social.repository;

import tec.bd.social.Rating;
import tec.bd.social.datasource.DBManager;
import tec.bd.social.service.RatingsServiceImpl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;

public class RatingsRepositoryImpl extends BaseRepository<Rating> implements  RatingsRepository{

    private static final String FIND_BY_RATING_ID_QUERY = "select id, todoid, ratingvalue, createdat, clientid from rating where id = ?";
    private static final String CALCULATE_AVG_PROC = "{call calculate_rating_average()}";
    private static final String ADD_RATING = "{call rate_todo(?,?,?,?)}";
    private static final String DELETE_RATING = "{call delete_rating(?,?)}";

    public RatingsRepositoryImpl(DBManager dbManager){
        super(dbManager);
    }

    @Override
    public Rating findById(int ratingId) {
        try {
            var connection = this.connect();
            var statement = connection.prepareStatement(FIND_BY_RATING_ID_QUERY);
            statement.setInt(1, ratingId);
            var resultSet = this.query(statement);
            while(resultSet.next()) {
                var rating = toEntity(resultSet);
                return rating;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public float findAverage(){
        try {
            var connection = this.connect();
            CallableStatement statement = connection.prepareCall(CALCULATE_AVG_PROC);

            var resultSet = this.query(statement);
            while(resultSet.next()) {
                var ratingAvg = resultSet.getFloat("ratingAvg");
                return ratingAvg;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    @Override
    public Rating addRating(String todoId, String clientId, Date date, int rate) {
        try {
            var connection = this.connect();
            CallableStatement statement = connection.prepareCall(ADD_RATING);
            statement.setString(1, clientId);
            statement.setString(2, todoId);
            statement.setDate(3, (java.sql.Date) date);
            statement.setInt(4, rate);
            var resultSet = this.query(statement);
            while(resultSet.next()) {
                var rating =toEntity(resultSet);
                return rating;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String deleteTodo(String clientId, String todoId) {
        try {
            var connection = this.connect();
            CallableStatement statement = connection.prepareCall(DELETE_RATING);
            statement.setString(1,todoId);
            statement.setString(2,clientId);
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
    public Rating toEntity(ResultSet resultSet) {
        try {
            var rating = new Rating(
                    resultSet.getInt("id"),
                    resultSet.getInt("ratingValue"),
                    resultSet.getString("todoId"),
                    resultSet.getString("clientid"),
                    resultSet.getDate("createdat")
            );
            return rating;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
