package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import school.journal.controller.util.callable.*;
import school.journal.controller.util.ErrorObject;
import school.journal.service.exception.ServiceException;

import static org.springframework.http.HttpStatus.*;
import static school.journal.controller.util.ErrorObject.CRITICAL_ERROR;

public abstract class BaseController<T> {
    ResponseEntity delete(Callable_NoResult_ParamsInt operation, int i, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            operation.call(i);
            resultResponse = createResponseEntity(OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity getOne(Callable_ParamsInt<T> operation, int i, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(i), OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity createOrUpdate(Callable_ParamsT<T> operation, T obj, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(obj), OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity read(Callable_ResultList<T> operation, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(), OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(Callable_ResultList_ParamsInt<T> operation, int i, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(i), OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(Callable_ParamsIntInt<T> operation, int a, int b, String errorMessage, Logger logger, boolean _) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(a, b), OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(Callable_NoParams<T> operation, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(), OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(Callable_ParamsIntBoolean<T> operation, int i, boolean f, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(i, f), OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(Callable_ParamsIntString<T> operation, int i, String s, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(i, s), OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    private ResponseEntity createResponseEntity(Object data, HttpStatus status) {
        return new ResponseEntity(data, status);
    }

    private ResponseEntity createResponseEntity(HttpStatus status) {
        return new ResponseEntity(status);
    }

    //##############################

    ResponseEntity doResponse(Callable_ResultList_ParamsIntInt<T> operation, int a, int b,
                              String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(a, b), OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(Callable_ResultList_ParamsIntIntInt<T> operation, int a, int b, int c,
                              String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(a, b, c), OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(Callable_ParamsInt<T> operation, int i, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(i), OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(Callable_NoResult_ParamsInt operation, int i, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            operation.call(i);
            resultResponse = createResponseEntity(OK);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

}
