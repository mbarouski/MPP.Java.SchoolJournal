package school.journal.service.exception;

import java.util.ArrayList;
import java.util.List;

public class ServiceException extends Exception {
    private Object causeObject;
    private List<String> errorMessageList = new ArrayList<>();

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(Object causeObject) {
        this.causeObject = causeObject;
    }
}
