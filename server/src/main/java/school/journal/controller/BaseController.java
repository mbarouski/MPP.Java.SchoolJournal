package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import school.journal.controller.util.callable.*;
import school.journal.controller.util.ErrorObject;
import school.journal.service.exception.ClassifiedServiceException;
import school.journal.service.exception.ServiceException;

import static org.springframework.http.HttpStatus.*;
import static school.journal.controller.util.ErrorObject.CRITICAL_ERROR;

public abstract class BaseController<T> {

    public static final ResponseEntity UNAUTHORIZED_RESPONSE = new ResponseEntity(HttpStatus.UNAUTHORIZED);

    ResponseEntity delete(CallableWithoutResultWithParamsInt operation, int i, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            operation.call(i);
            resultResponse = createResponseEntity(OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType(), BAD_REQUEST);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity getOne(CallableWithParamsInt<T> operation, int i, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(i), OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType(), BAD_REQUEST);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity createOrUpdate(CallableWithTParam<T> operation, T obj, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(obj), OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType() + "", BAD_REQUEST);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity read(CallableWithResultList<T> operation, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(), OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType() + "", BAD_REQUEST);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(CallableWithResultListWithParamsInt<T> operation, int i, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(i), OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType() + "", BAD_REQUEST);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(CallableWithParamsIntInt<T> operation, int a, int b, String errorMessage, Logger logger, boolean _) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(a, b), OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType() + "", BAD_REQUEST);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(CallableWithoutParams<T> operation, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(), OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType() + "", BAD_REQUEST);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(CallableWithParamsIntBoolean<T> operation, int i, boolean f, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(i, f), OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType() + "", BAD_REQUEST);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(CallableWithParamsIntString<T> operation, int i, String s, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(i, s), OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType() + "", BAD_REQUEST);
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
        HttpHeaders h = new HttpHeaders();
        h.set("Content-type","application/json;charset=UTF-8");
        return new ResponseEntity(data,h, status);
    }

    private ResponseEntity createResponseEntity(HttpStatus status) {
        return new ResponseEntity(status);
    }


    ResponseEntity doResponse(CallableWithResultListWithParamsIntInt<T> operation, int a, int b,
                              String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(a, b), OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType() + "", BAD_REQUEST);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(CallableWithResultListWithParamsIntIntInt<T> operation, int a, int b, int c,
                              String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(a, b, c), OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType() + "", BAD_REQUEST);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(CallableWithParamsInt<T> operation, int i, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = createResponseEntity(operation.call(i), OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType() + "", BAD_REQUEST);
        } catch (ServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(new ErrorObject(errorMessage), BAD_REQUEST);
        } catch (Exception exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    ResponseEntity doResponse(CallableWithoutResultWithParamsInt operation, int i, String errorMessage, Logger logger) {
        ResponseEntity resultResponse = null;
        try {
            operation.call(i);
            resultResponse = createResponseEntity(OK);
        } catch (ClassifiedServiceException exc) {
            logger.error(exc);
            resultResponse = createResponseEntity(exc.getExceptionType() + "", BAD_REQUEST);
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
