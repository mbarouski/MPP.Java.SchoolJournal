package school.journal.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobilePhoneValidator {
    private static Pattern PHONE_PATTERN = Pattern.compile("\\+375[0-9]{9}");

    public static boolean validate(String phone) {
        Matcher m = PHONE_PATTERN.matcher(phone);
        return m.matches();
    }
}
