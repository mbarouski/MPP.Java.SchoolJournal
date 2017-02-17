package school.journal.service.exception;

import school.journal.entity.Role;
import school.journal.service.IRoleService;
import school.journal.service.ServiceAbstractClass;

import java.util.List;

public class ServiceException extends Exception {
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
}
