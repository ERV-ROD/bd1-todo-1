package tec.bd.todo.auth;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tec.bd.todo.auth.service.SessionService;
import tec.bd.todo.auth.service.SessionServiceImpl;

import java.util.*;

import static spark.Spark.*;


public class AuthApi {

    private final static Logger LOG = LoggerFactory.getLogger(AuthApi.class);

    public static void main(String[] args ) {

        var webAppContext = WebApplicationContext.init();
        SessionService sessionService = webAppContext.getSessionService();
        Gson gson = webAppContext.getGson();

        port(8080);
        options("/", (request, response) -> {
            response.header("Content-Type", "application/json");
            return Map.of(
                    "message", "AUTH API");
        }, gson::toJson);

        get("/clients", (request, response) -> {
            return sessionService.getAllClients();
        }, gson::toJson);

        post("/clients", (request, response) -> {
            try {
                var credentials = gson.fromJson(request.body(), ClientCredentials.class);
                sessionService.addNewClient(credentials);
                return credentials;
            }catch(SessionServiceImpl.CredentialsException e){
                response.status(400);
                return Map.of("Message", "El cliente ya existe");
            }
        }, gson::toJson);

        delete("/clients/:client-id", (request, response) -> {
            try {
                var clientId = request.params("client-id");
                sessionService.deleteClient(clientId);
                response.status(200);
                return Map.of("Deleted", "OK");
            }catch (Exception e){
                response.status(404);
                return Map.of("Mesage", "404 Not Found");
            }
        }, gson::toJson);

        get("/sessions", (request, response) -> {
           return sessionService.getAllSessions();
        }, gson::toJson);

        post("/sessions", (request, response) -> {
            var credentials = gson.fromJson(request.body(), ClientCredentials.class);
            try {
                var session = sessionService.newSession(credentials);
                response.status(200);
                return session;
            } catch (SessionServiceImpl.CredentialsException e) {
                response.status(400);
                return Map.of("Message", "Bad Credentials");
            }
        }, gson::toJson);

        get("/sessions/validate", (request, response) -> {
            try{
                var sessionParam = request.queryParams("session");
                response.status(200);
                return sessionService.validateSession(sessionParam);
            }catch(SessionServiceImpl.CredentialsException e){
                response.status(400);
                return Map.of("Message", "Bad Request");
            }
        }, gson::toJson);
    }
}
