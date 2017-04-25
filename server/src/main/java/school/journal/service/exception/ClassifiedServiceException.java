package school.journal.service.exception;

import school.journal.controller.util.ExceptionEnum;

public class ClassifiedServiceException extends ServiceException {

    private ExceptionEnum exceptionType;

    public ClassifiedServiceException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum + "");
        this.exceptionType = exceptionEnum;
    }

    public ClassifiedServiceException(ExceptionEnum exceptionEnum, Throwable cause) {
        super(exceptionEnum + "", cause);
        this.exceptionType = exceptionEnum;
    }

    public ClassifiedServiceException(Throwable cause) {
        super(cause);
    }

    public ClassifiedServiceException(Object causeObject) {
        super(causeObject);
    }

    public ExceptionEnum getExceptionType() {
        return exceptionType;
    }
}
