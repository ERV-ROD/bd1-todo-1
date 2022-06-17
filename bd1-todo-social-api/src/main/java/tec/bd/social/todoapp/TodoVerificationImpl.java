package tec.bd.social.todoapp;

public class TodoVerificationImpl implements TodoVerification {
    private TodoVerificationResource todoVerificationResource;

    public TodoVerificationImpl(TodoVerificationResource todoVerificationResource) {
        this.todoVerificationResource = todoVerificationResource;
    }

    public TodoRecord validateTodo(String todoId,String sessionId) {

        try {
            return todoVerificationResource.validateInServer(sessionId,todoId).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            return new TodoRecord(todoId, Status.NOT_EXIST);
        }
    }
}


