package tec.bd.social;

import com.google.gson.Gson;
import tec.bd.social.authentication.SessionStatus;
import tec.bd.social.service.ReviewsService;


import java.util.Map;

import static spark.Spark.*;

public class SocialApi
{
    public static void main( String[] args )
    {

        WebApplicationContext context = WebApplicationContext.init();

        var authenticationClient = context.getAuthenticationClient();
        var todoVerification = context.getTodoVerification();
        var ratingsService = context.getRatingsService();
        var reviewsService = context.getReviewsService();


        Gson gson = context.getGson();
        port(8082);

        // Autentication
        before((request, response) -> {
            var sessionParam = request.headers("x-session-id");
            if(null == sessionParam) {
                halt(401, "Unauthorized");
            }
            var session = authenticationClient.validateSession(sessionParam);
            session.getClientId();
            if(session.getStatus() == SessionStatus.INACTIVE) {
                halt(401, "Unauthorized");
            }
        });

        // Agregar un rating
        post("ratings/:todo-id", (request, response) -> {
            var todoId = request.params("todo-id");
            var sessionParam = request.headers("x-session-id");
            var ratingParams = gson.fromJson(request.body(), Rating.class);
            var session = authenticationClient.validateSession(sessionParam);
            var todoExists = todoVerification.validateTodo(todoId,sessionParam);
            ratingParams.setTodoId(request.params("todo-id"));
            if( todoExists != null){
                try {
                    var rating = ratingsService.addNewRating(todoId,session.getClientId(),ratingParams.getCreatedAt(),ratingParams.getratingValue());

                    if (rating != null){
                        response.status(200);
                        return rating;
                    }
                    response.status(400);
                    return Map.of("Message", "El cliente ya ingresó un rating");
                } catch (Exception e) {
                    response.status(400);
                    return Map.of("Message", "El proceso falló");
                }
            }
            response.status(404);
            return Map.of("Message", "Todo no encontrado");
        }, gson::toJson);




        options("/", (request, response) -> {
            response.header("Content-Type", "application/json");
            return Map.of(
                    "message", "Social API V1");
        }, gson::toJson);

        get("/ratings/:rating-id", (request,response) ->{
            var ratingIdParam = request.params("rating-id");

            var ratingId = Integer.parseInt(ratingIdParam);

            var rating = ratingsService.getRating(ratingId);

            if(null != rating){
                return rating;
            }

            response.status((404));
            return  Map.of();

            /*response.header("Content-Type", "application/json");
            return Map.of(
                    "message", "Get rating for Todo-id " + ratingId);*/
        }, gson::toJson);

        //Obtiene el valor promedio de los ratings de un todoId
        get("todos/:todo-id/rating", (request,response) ->{
            var todoId = request.params("todo-id");

            var ratingAvg = ratingsService.getRatingAverage(todoId);
            response.status(200);
            response.header("Content-Type", "application/json");
            return Map.of(
                    "todo-id", todoId,
                    "rating", ratingAvg
            );
        }, gson::toJson);


        // eliminar rating
        delete("ratings/:todo-id",(request,response)->{
            var todoId = request.params("todo-id");
            var sessionParam = request.headers("x-session-id");
            var session = authenticationClient.validateSession(sessionParam);
            try{
                String status = ratingsService.deleteTodo(session.getClientId(),todoId);
                if(status.equals("Rating eliminado")){
                    response.status(200);
                    return Map.of("Message", "200 - Ok");
                }
                response.status(404);
                return Map.of("Message", "400 - Not Found");
            }catch(Exception e){
                response.status(404);
                return Map.of("Message", "El procedimiento falló");
            }
        },gson::toJson);

        /*================================================================================================*/
        /*                                          Reviews                                               */
        /*================================================================================================*/

        // Obtener todos los reviews de un todo en particular
        get("reviews/:todo-id",(request, response)-> {
            var todoId = request.params("todo-id");
            try {
                var reviews = reviewsService.getReviews(todoId);
                response.status(200);
                return reviews;
            } catch (Exception e) {
                response.status(400);
                return Map.of("Message", "Bad Credentials");
            }
        },gson::toJson);

        // Agregar un review
        post("/reviews/:todo-id",(request, response)->{
            var todoId = request.params("todo-id");
            var sessionParam = request.headers("x-session-id");
            var reviewParams = gson.fromJson(request.body(), Review.class);
            var session = authenticationClient.validateSession(sessionParam);
            var todoExists = todoVerification.validateTodo(todoId,sessionParam);
            if( todoExists != null) {
                reviewParams.setTodoId(request.params("todo-id"));
                try {
                    var review = reviewsService.addNewReview(reviewParams.getReviewText(), session.getClientId(), todoId, reviewParams.getCreatedAt());

                    if (review != null) {
                        response.status(200);
                        return review;
                    }
                    response.status(400);
                    return Map.of("Message", "El cliente ya ingresó un review");

                } catch (Exception e) {
                    response.status(400);
                    return Map.of("Message", "El proceso falló");
                }
            }
            response.status(404);
            return Map.of("Message", "Todo no encontrado");
        }, gson::toJson);


        //Eliminar un review
        delete("reviews/:todo-id",(request,response)->{
            var todoId = request.params("todo-id");
            var sessionParam = request.headers("x-session-id");
            var session = authenticationClient.validateSession(sessionParam);
            try{
                String status = reviewsService.deleteReview(session.getClientId(),todoId);
                if(status.equals("Review eliminado")){
                    response.status(200);
                    return Map.of("Message", "200 - Ok");
                }
                response.status(404);
                return Map.of("Message", "404 - Not Found");
            }catch(Exception e){
                response.status(404);
                return Map.of("Message", "El procedimiento falló");
            }
        },gson::toJson);

        //Actualizar un Review

        put("/reviews/:todo-id",(request, response) -> {
            var todoId = request.params("todo-id");
            var sessionParam = request.headers("x-session-id");
            var session = authenticationClient.validateSession(sessionParam);
            var reviewParams = gson.fromJson(request.body(), Review.class);
            try{
                Review result =  reviewsService.updateReview(session.getClientId(),todoId,reviewParams.getReviewText(),reviewParams.getCreatedAt());

                if(result == null){
                    response.status(404);
                    return Map.of("Message", "404 - Not Found");
                }
                response.status(200);
                return Map.of("Message", "200 - Ok");
            }catch(Exception e){
                response.status(404);
                return Map.of("Message", "El procedimiento falló");
            }
        },gson::toJson);
    }
}
