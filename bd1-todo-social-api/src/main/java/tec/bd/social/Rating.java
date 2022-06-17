package tec.bd.social;

import java.sql.Date;

public class Rating {
    private int ratingId;
    private int rating;
    private String todoId;
    private String clientId;
    private Date createdAt;

    public int getRatingId() {
        return ratingId;
    }

    public Rating(int ratingId, int ratingValue, String todoId, String clientId, Date createdAt) {
        this.ratingId = ratingId;
        this.rating = ratingValue;
        this.todoId = todoId;
        this.clientId = clientId;
        this.createdAt = createdAt;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getratingValue() {
        return rating;
    }

    public void setRatingValue(int rating) {
        this.rating = rating;
    }

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
