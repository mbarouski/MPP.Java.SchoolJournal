package school.journal.utils;


import school.journal.utils.exception.ValidationException;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateServiceUtils {

    private static Pattern PHONE_PATTERN = Pattern.compile("\\+375[0-9]{9}");
    private static Pattern EMAIL_PATTERN = Pattern.compile("[0-9a-zA-Z]+@[0-9a-zA-Z]");

    public static void validateNullableId(Integer id, String column) throws ValidationException {
        if (id != null) validateId(id, column);
    }

    public static void validateId(int id, String column) throws ValidationException {
        if (id <= 0) throw new ValidationException("Invalid " + column + "Id");
    }

    public static void validateNullableString(String string, String column) throws ValidationException {
        if (string == null) return;
        if (string.isEmpty()) throw new ValidationException("Invalid " + column);
    }

    public static void validateString(String string, String column) throws ValidationException {
        if (string == null) throw new ValidationException("Invalid (null)" + column);
        validateNullableString(string, column);
    }

    public static void validatePhone(String phone) throws ValidationException {
        validateNullableString(phone, "Phone");
        if (phone != null) {
            Matcher m = PHONE_PATTERN.matcher(phone);
            if (!m.matches()) {
                throw new ValidationException("Phone isn't correct.");
            }
        }
    }

    public static void validateEmail(String email) throws ValidationException {
        validateString(email, "Email");
        Matcher m = EMAIL_PATTERN.matcher(email);
        if (!m.matches()) {
            throw new ValidationException("Email isn't correct.");
        }
    }

    public static void validateDatePeriod(Date startDate, Date endDate) throws ValidationException{
        if (startDate.after(endDate)) {
            throw new ValidationException("Invalid time period");
        }
    }
}
