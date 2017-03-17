package school.journal.controller.util;

public class ErrorObject {
    private String message;

    public ErrorObject(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static final ErrorObject CRITICAL_ERROR = new ErrorObject("Some critical error");
}
