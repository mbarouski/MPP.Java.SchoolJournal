package school.journal.utils;

import school.journal.service.exception.ServiceException;

public class ValidateServiceUtils {

    public static void validateId(int id, String column)throws ServiceException  {
        if (id <= 0){
            throw new ServiceException("Invalid " + column + "Id");
        }
    }

    public static void validateString(String string,String message) throws ServiceException{
        if( (string == null) || (string.isEmpty())){
            throw new ServiceException(message);
        }

    }

}
