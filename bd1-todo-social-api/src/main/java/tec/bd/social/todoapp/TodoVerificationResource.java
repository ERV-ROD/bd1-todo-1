package tec.bd.social.todoapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface TodoVerificationResource {
    @GET("/api/v1/todos/{todo-id}")
    Call<TodoRecord> validateInServer(@Header("x-session-id") String sessionId, @Path("todo-id") String todoId);
}