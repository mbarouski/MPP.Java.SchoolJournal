package school.journal.controller.util;

public class ErrorObject {
    private String message;

    public ErrorObject(String message) {
        this.message = message;
    }

    public ErrorObject(String message,Exception cause){
        this.message = String.format("%1$s, cause: %2$s",message,cause.getMessage());
    }

    public ErrorObject(String subjectName,String methodEssence,Exception cause) {
        this.message = String.format("Error in %1$s while %2$s, cause: %3$s", subjectName, methodEssence, cause.getMessage());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static final ErrorObject CRITICAL_ERROR = new ErrorObject("Some critical error");
}
