package tec.bd.social.todoapp;

public interface TodoVerification {
    TodoRecord validateTodo(String todoId,String sessionId);
}
