package school.journal.utils;

import school.journal.service.exception.ServiceException;

public class ValidateServiceUtils {

    public static void validateNullableId(Integer id, String column) throws ServiceException {
        if (id != null) validateId(id, column);
    }

    public static void validateId(int id, String column) throws ServiceException {
        if (id <= 0) throw new ServiceException("Invalid " + column + "Id");
    }

    public static void validateNullableString(String string, String column) throws ServiceException {
        if (string.isEmpty()) throw new ServiceException("Invalid " + column);
    }

    public static void validateString(String string, String column) throws ServiceException {
        if (string == null) throw new ServiceException("Invalid (null)" + column);
        validateNullableString(string, column);
    }

}
