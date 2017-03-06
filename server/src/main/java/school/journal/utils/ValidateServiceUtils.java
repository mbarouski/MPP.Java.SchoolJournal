package school.journal.utils;

import school.journal.service.exception.ServiceException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateServiceUtils {

    private static Pattern PHONE_PATTERN = Pattern.compile("\\+375[0-9]{9}");

    public static void validateNullableId(Integer id, String column) throws ServiceException {
        if (id != null) validateId(id, column);
    }

    public static void validateId(int id, String column) throws ServiceException {
        if (id <= 0) throw new ServiceException("Invalid " + column + "Id");
    }

    public static void validateNullableString(String string, String column) throws ServiceException {
        if (string == null) return;
        if (string.isEmpty()) throw new ServiceException("Invalid " + column);
    }

    public static void validateString(String string, String column) throws ServiceException {
        if (string == null) throw new ServiceException("Invalid (null)" + column);
        validateNullableString(string, column);
    }

    public static boolean validate(String phone) {
        Matcher m = PHONE_PATTERN.matcher(phone);
        return m.matches();
    }
}
